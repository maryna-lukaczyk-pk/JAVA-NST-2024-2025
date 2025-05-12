package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.getFirst().getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID when it exists")
    void testGetTaskById_WhenExists() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Example Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Example Task", result.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void testGetTaskById_WhenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(99L);
        });

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository).findById(99L);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task taskToCreate = new Task();
        taskToCreate.setTitle("New Task");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");

        when(taskRepository.save(taskToCreate)).thenReturn(savedTask);

        Task result = taskService.createTask(taskToCreate);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Task", result.getTitle());
        verify(taskRepository).save(taskToCreate);
    }

    @Test
    @DisplayName("Should update task when it exists")
    void testUpdateTask_WhenExists() {
        Long taskId = 1L;

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");

        Task updatedDetails = new Task();
        updatedDetails.setTitle("Updated Title");
        updatedDetails.setDescription("Updated Description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(taskId, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    void testUpdateTask_WhenNotFound() {
        Long taskId = 999L;
        Task details = new Task();
        details.setTitle("Test");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(taskId, details);
        });

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTask_WhenExists() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(new Task()));

        taskService.deleteTask(taskId);

        verify(taskRepository).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent task")
    void testDeleteTask_WhenNotFound() {
        Long taskId = 404L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(taskId);
        });

        assertEquals("Task not found", exception.getMessage());
        verify(taskRepository, never()).deleteById(any());
    }
}