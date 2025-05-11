package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTests {
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskService = new TaskService(taskRepository, projectRepository);
    }

    @Test
    @DisplayName("It should create a task with existing project")
    void createTaskWithExistingProject() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setProject(project);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(project, result.getProject());
        verify(projectRepository, times(1)).findById(projectId);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("It should create a task without project ID")
    void createTaskWithoutProjectId() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setProject(new Project());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(projectRepository, never()).findById(any());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("It should throw exception when creating task with non-existent project")
    void createTaskWithNonExistentProject() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        Task task = new Task();
        task.setProject(project);

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            taskService.createTask(task);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(projectRepository, times(1)).findById(projectId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("It should find tasks by project ID")
    void findTasksByProjectId() {
        Long projectId = 1L;
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(taskRepository.findByProjectId(projectId)).thenReturn(tasks);

        List<Task> result = taskService.findTasksByProjectId(projectId);

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, times(1)).findByProjectId(projectId);
    }

    @Test
    @DisplayName("It should throw exception when finding tasks for non-existent project")
    void findTasksByProjectIdNotFound() {
        Long projectId = 1L;

        when(projectRepository.existsById(projectId)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            taskService.findTasksByProjectId(projectId);
        });

        assertEquals("404 NOT_FOUND \"Projekt o ID 1 nie został znaleziony\"", exception.getMessage());
        verify(projectRepository, times(1)).existsById(projectId);
        verify(taskRepository, never()).findByProjectId(any());
    }

    @Test
    @DisplayName("It should find task by ID")
    void findTaskById() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.findTaskById(taskId);

        assertTrue(result.isPresent());
        assertEquals(taskId, result.get().getId());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("It should return empty Optional when task not found")
    void findTaskByIdNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findTaskById(taskId);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("It should find all tasks")
    void findAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.findAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("It should update an existing task")
    void updateTask() {
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");

        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Title");
        taskDetails.setDescription("Updated Description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        Task result = taskService.updateTask(taskId, taskDetails);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("It should throw exception when updating non-existent task")
    void updateTaskNotFound() {
        Long taskId = 1L;
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            taskService.updateTask(taskId, taskDetails);
        });

        assertEquals("404 NOT_FOUND \"Zadanie o ID 1 nie zostało znalezione\"", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("It should delete an existing task")
    void deleteTask() {
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("It should throw exception when deleting non-existent task")
    void deleteTaskNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            taskService.deleteTask(taskId);
        });

        assertEquals("404 NOT_FOUND \"Zadanie o ID 1 nie zostało znalezione\"", exception.getMessage());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).delete(any(Task.class));
    }
}