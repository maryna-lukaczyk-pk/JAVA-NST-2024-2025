package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repozytorium.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    void testGetAllProjects() {
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("Project 1");

        Project p2 = new Project();
        p2.setId(2L);
        p2.setName("Project 2");

        List<Project> mockProjects = Arrays.asList(p1, p2);

        when(projectRepository.findAll()).thenReturn(mockProjects);

        List<Project> result = projectService.getAllProjects();
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getName());
    }

    @Test
    void testGetProjectByIdSuccess() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);
        assertEquals("Test Project", result.getName());
    }

    @Test
    void testGetProjectByIdNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    void testCreateProject() {
        Project project = new Project();
        project.setName("New Project");

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("New Project");

        when(projectRepository.save(project)).thenReturn(savedProject);

        Project result = projectService.createProject(project);
        assertNotNull(result.getId());
        assertEquals("New Project", result.getName());
    }

    @Test
    void testUpdateProjectSuccess() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("Old Name");

        Project updatedDetails = new Project();
        updatedDetails.setName("New Name");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.updateProject(1L, updatedDetails);
        assertEquals("New Name", result.getName());
    }

    @Test
    void testUpdateProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Project updatedDetails = new Project();
        updatedDetails.setName("New Name");

        assertThrows(NoSuchElementException.class, () -> projectService.updateProject(1L, updatedDetails));
    }

    @Test
    void testDeleteProjectSuccess() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProjectNotFound() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> projectService.deleteProject(1L));
    }
    @Test
    void testSetAndGetProjectUsers() {
        Project project;
        Users user1;
        Users user2;
        Tasks task1;
        Tasks task2;
        project = new Project();

        user1 = new Users();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new Users();
        user2.setId(2L);
        user2.setUsername("user2");

        task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("Task 1");

        task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("Task 2");
        Set<Users> usersSet = new HashSet<>();
        usersSet.add(user1);
        usersSet.add(user2);

        project.setProjectUsers(usersSet);
        Set<Users> result = project.getProjectUsers();

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void testSetAndGetProjectTasks() {
        Project project;
        Users user1;
        Users user2;
        Tasks task1;
        Tasks task2;
        project = new Project();

        user1 = new Users();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new Users();
        user2.setId(2L);
        user2.setUsername("user2");

        task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("Task 1");

        task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("Task 2");
        Set<Tasks> tasksSet = new HashSet<>();
        tasksSet.add(task1);
        tasksSet.add(task2);

        project.setProjectTasks(tasksSet);
        Set<Tasks> result = project.getProjectTasks();

        assertEquals(2, result.size());
        assertTrue(result.contains(task1));
        assertTrue(result.contains(task2));
    }
}
