package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProjectServiceTests {
    private ProjectRepository projectRepository;
    private ProjectService projectService;
    private TaskRepository taskRepository;
    private TaskService taskService;
    private UserRepository userRepository;

    @BeforeEach()
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = Mockito.mock(TaskService.class);
        userRepository = Mockito.mock(UserRepository.class);
        projectService = new ProjectService(projectRepository, taskRepository, taskService, userRepository);
    }

    @Test
    @DisplayName("It should return all projects")
    void getAllProjects() {
        Project project1 = new Project();
        Project project2 = new Project();

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.findAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("It should find project by ID")
    void findProjectById() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.findProjectById(projectId);

        assertTrue(result.isPresent());
        assertEquals(projectId, result.get().getId());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    @DisplayName("It should return empty Optional when project not found")
    void findProjectByIdNotFound() {
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.findProjectById(projectId);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    @DisplayName("It should create a new project")
    void createProject() {
        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.createProject(project);

        assertNotNull(result);
        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("It should update an existing project")
    void updateProject() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old Name");

        Project projectDetails = new Project();
        projectDetails.setName("Updated Name");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(existingProject);

        Project result = projectService.updateProject(projectId, projectDetails);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("It should throw exception when updating non-existent project")
    void updateProjectNotFound() {
        Long projectId = 1L;
        Project projectDetails = new Project();
        projectDetails.setName("Updated Name");

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.updateProject(projectId, projectDetails);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(projectRepository, times(1)).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should delete a project and its tasks")
    void deleteProject() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).findById(projectId);
        verify(taskRepository, times(1)).deleteByProjectId(projectId);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    @DisplayName("It should throw exception when deleting non-existent project")
    void deleteProjectNotFound() {
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.deleteProject(projectId);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(projectRepository, times(1)).findById(projectId);
        verify(taskRepository, never()).deleteByProjectId(any());
        verify(projectRepository, never()).delete(any(Project.class));
    }

    @Test
    @DisplayName("It should get tasks for a project")
    void getProjectTasks() {
        Long projectId = 1L;
        List<Task> tasks = Arrays.asList(new Task(), new Task());

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(taskService.findTasksByProjectId(projectId)).thenReturn(tasks);

        List<Task> result = projectService.getProjectTasks(projectId);

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskService, times(1)).findTasksByProjectId(projectId);
    }

    @Test
    @DisplayName("It should throw exception when getting tasks for non-existent project")
    void getProjectTasksProjectNotFound() {
        Long projectId = 1L;

        when(projectRepository.existsById(projectId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            projectService.getProjectTasks(projectId);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskService, never()).findTasksByProjectId(any());
    }

    @Test
    @DisplayName("It should assign user to project")
    void assignUserToProject() {
        Long projectId = 1L;
        Long userId = 2L;

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(new ArrayList<>());

        User user = new User();
        user.setId(userId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        projectService.assignUserToProject(projectId, userId);

        assertTrue(project.getUsers().contains(user));
        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("It should assign user to project with null users list")
    void assignUserToProjectWithNullUsersList() {
        Long projectId = 1L;
        Long userId = 2L;

        Project project = new Project();
        project.setId(projectId);
        project.setUsers(null);

        User user = new User();
        user.setId(userId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        projectService.assignUserToProject(projectId, userId);

        assertNotNull(project.getUsers());
        assertTrue(project.getUsers().contains(user));
        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("It should not assign user if already assigned to project")
    void assignUserToProjectAlreadyAssigned() {
        Long projectId = 1L;
        Long userId = 2L;

        User user = new User();
        user.setId(userId);

        Project project = new Project();
        project.setId(projectId);
        List<User> users = new ArrayList<>();
        users.add(user);
        project.setUsers(users);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        projectService.assignUserToProject(projectId, userId);

        assertEquals(1, project.getUsers().size());
        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should throw exception when assigning user to non-existent project")
    void assignUserToProjectNotFound() {
        Long projectId = 1L;
        Long userId = 2L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            projectService.assignUserToProject(projectId, userId);
        });

        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, never()).findById(any());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("It should throw exception when assigning non-existent user to project")
    void assignNonExistentUserToProject() {
        Long projectId = 1L;
        Long userId = 2L;

        Project project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> {
            projectService.assignUserToProject(projectId, userId);
        });

        verify(projectRepository, times(1)).findById(projectId);
        verify(userRepository, times(1)).findById(userId);
        verify(projectRepository, never()).save(any(Project.class));
    }
}