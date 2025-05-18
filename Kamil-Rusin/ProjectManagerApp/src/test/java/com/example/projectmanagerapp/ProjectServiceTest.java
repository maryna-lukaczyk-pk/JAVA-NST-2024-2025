package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.services.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectService projectService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        taskRepository = mock(TaskRepository.class);
        projectService = new ProjectService(projectRepository, userRepository, taskRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setName("Project 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAll();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Sample Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sample Project", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project project = new Project();
        project.setName("New Project");

        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectService.createProject(project);

        assertEquals("New Project", savedProject.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateExistingProject() {
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old");

        Project updated = new Project();
        updated.setName("Updated");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.updateProject(1L, updated);

        assertEquals("Updated", result.getName());
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("Should save new project when updating non-existing one")
    void testUpdateNonExistingProject() {
        Project project = new Project();
        project.setName("Example");

        when(projectRepository.findById(5L)).thenReturn(Optional.empty());
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.updateProject(5L, project);

        assertEquals("Example", result.getName());
        verify(projectRepository).save(project);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        projectService.deleteProject(2L);
        verify(projectRepository).deleteById(2L);
    }

    @Test
    @DisplayName("Should assign user to project")
    void testAssignUserToProject() {
        Long userId = 2L;
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);

        User user = new User();
        user.setId(userId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        projectService.assignUserToProject(userId, projectId);

        assertTrue(project.getUsers().contains(user));
        assertTrue(user.getProjects().contains(project));
        verify(projectRepository).save(project);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Should assign task to project")
    void testAssignTaskToProject() {
        Long taskId = 3L;
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);

        Task task = new Task();
        task.setId(taskId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        projectService.assignTaskToProject(taskId, projectId);

        assertTrue(project.getTasks().contains(task));
        assertEquals(project, task.getProjectId());
        verify(projectRepository).save(project);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw exception when project not found while assigning user")
    void testAssignUserToProject_ProjectNotFound() {
        Long userId = 2L;
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignUserToProject(userId, projectId));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when user not found while assigning to project")
    void testAssignUserToProject_UserNotFound() {
        Long userId = 2L;
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignUserToProject(userId, projectId));

        assertEquals("User with id 2 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when project not found while assigning task")
    void testAssignTaskToProject_ProjectNotFound() {
        Long taskId = 2L;
        Long projectId = 1L;

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignTaskToProject(taskId, projectId));

        assertEquals("Project with id 1 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when task not found while assigning to project")
    void testAssignTaskToProject_TaskNotFound() {
        Long taskId = 2L;
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                projectService.assignTaskToProject(taskId, projectId));

        assertEquals("Task with id 2 not found", exception.getMessage());
    }
}
