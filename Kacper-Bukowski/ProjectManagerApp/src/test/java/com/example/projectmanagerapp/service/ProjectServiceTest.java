package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectService = new ProjectService(projectRepository, userRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void getAllProjects() {
        Project project1 = new Project();
        project1.setName("TestProject1");

        Project project2 = new Project();
        project2.setName("TestProject2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new project")
    void createProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project createdProject = projectService.createProject(project);

        assertNotNull(createdProject);
        assertEquals("NewProject", createdProject.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should return project by ID")
    void getProjectById() {
        Project project = new Project();
        project.setName("TestProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project foundProject = projectService.getProjectById(1L);

        assertNotNull(foundProject);
        assertEquals("TestProject", foundProject.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found by ID")
    void getProjectById_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.getProjectById(1L);
        });

        assertEquals("Project not found with id: 1", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update project")
    void updateProject() {
        Project existingProject = new Project();
        existingProject.setName("OldProjectName");

        User user = new User();
        user.setUsername("TestUser");
        Set<User> users = new HashSet<>();
        users.add(user);

        Project updatedDetails = new Project();
        updatedDetails.setName("NewProjectName");
        updatedDetails.setUsers(users);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project updatedProject = projectService.updateProject(1L, updatedDetails);

        assertNotNull(updatedProject);
        assertEquals("NewProjectName", updatedProject.getName());
        assertEquals(1, updatedProject.getUsers().size());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should delete project")
    void deleteProject() {
        Project project = new Project();
        project.setName("ProjectToDelete");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    @DisplayName("Should throw exception when project not found during delete")
    void deleteProject_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.deleteProject(1L);
        });

        assertEquals("Project not found with id: 1", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, never()).delete(any(Project.class));
    }

    @Test
    @DisplayName("Should throw exception when project not found during update")
    void updateProject_NotFound() {
        Project updatedDetails = new Project();
        updatedDetails.setName("UpdatedProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(1L, updatedDetails);
        });

        assertEquals("Project not found with id: 1", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should add a user to a project")
    void addUserToProject() {
        Project project = new Project();
        setIdField(project, 1L);
        project.setName("TestProject");
        project.setUsers(new HashSet<>());

        User user = new User();
        setIdField(user, 1L);
        user.setUsername("TestUser");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project updatedProject = projectService.addUserToProject(1L, 1L);

        assertNotNull(updatedProject);
        assertNotNull(updatedProject.getUsers());
        assertEquals(1, updatedProject.getUsers().size());
        assertTrue(updatedProject.getUsers().contains(user));

        // Verify the repository methods were called
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should throw exception when project not found during add user")
    void addUserToProject_ProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.addUserToProject(1L, 1L);
        });

        assertEquals("Project not found with id: 1", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, never()).findById(anyLong());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found during add user")
    void addUserToProject_UserNotFound() {
        Project project = new Project();
        setIdField(project, 1L);
        project.setName("TestProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projectService.addUserToProject(1L, 1L);
        });

        assertEquals("User not found with id: 1", exception.getMessage());
        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should initialize users set when it's null")
    void addUserToProject_WithNullUsers() {
        Project project = new Project();
        setIdField(project, 1L);
        project.setName("TestProject");

        User user = new User();
        setIdField(user, 1L);
        user.setUsername("TestUser");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project updatedProject = projectService.addUserToProject(1L, 1L);

        assertNotNull(updatedProject);
        assertNotNull(updatedProject.getUsers(), "Users set should be initialized");
        assertEquals(1, updatedProject.getUsers().size(), "Users set should contain one user");
        assertTrue(updatedProject.getUsers().contains(user), "Users set should contain the added user");

        verify(projectRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project);
    }

    private void setIdField(Object entity, Long id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID field", e);
        }
    }
}
