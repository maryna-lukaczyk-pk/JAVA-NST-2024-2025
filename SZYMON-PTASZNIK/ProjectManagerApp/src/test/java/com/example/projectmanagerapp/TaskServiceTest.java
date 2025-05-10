package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
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
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty task list")
    void testGetAllTasksEmpty() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(0, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("New Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task created = taskService.createTask(task);
        assertEquals("New Task", created.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should delete a task by ID")
    void testDeleteTask() {
        long id = 1L;

        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteTask(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask() {
        long id = 2L;
        Task existing = new Task();
        existing.setTitle("Old Task");
        existing.setDescription("Old description");

        Task updated = new Task();
        updated.setTitle("Updated Task");
        updated.setDescription("New description");

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.updateTask(id, updated);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("New description", result.getDescription());
        verify(taskRepository).findById(id);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("Should throw when task not found on update")
    void testUpdateTaskNotFound() {
        long id = 3L;
        Task updated = new Task();
        updated.setTitle("Updated");

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(id, updated);
        });

        assertEquals("Task not found", ex.getMessage());
        verify(taskRepository).findById(id);
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        long id = 4L;
        Task task = new Task();
        task.setTitle("Sample Task");

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(id);
        assertEquals("Sample Task", result.getTitle());
        verify(taskRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw when task not found by ID")
    void testGetTaskByIdNotFound() {
        long id = 5L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(id);
        });

        assertEquals("Task not found", ex.getMessage());
        verify(taskRepository).findById(id);
    }
}
