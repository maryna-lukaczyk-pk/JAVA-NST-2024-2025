package com.example.projectmanagerapp.service;

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
    void testAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.allTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testTaskById() {
        Task task = new Task();
        task.setTitle("Sample Task");

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.taskById(1);

        assertEquals("Sample Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return null if task by ID not found")
    void testTaskByIdNotFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        Task result = taskService.taskById(99);

        assertNull(result);
        verify(taskRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Should save new task")
    void testNewTask() {
        Task task = new Task();
        task.setTitle("New Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task saved = taskService.newTask(task);

        assertEquals("New Task", saved.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update task if exists")
    void testUpdateTask() {
        Task existing = new Task();
        existing.setTitle("Old Title");

        Task updated = new Task();
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Desc");

        when(taskRepository.findById(1)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.update(1, updated);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        verify(taskRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should return null if updated task not found")
    void testUpdateTaskNotFound() {
        when(taskRepository.findById(99)).thenReturn(Optional.empty());

        Task result = taskService.update(99, new Task());

        assertNull(result);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete task by ID if exists")
    void testDeleteTask() {
        when(taskRepository.existsById(1)).thenReturn(true);

        taskService.delete(1);

        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should not delete task if not exists")
    void testDeleteTaskNotFound() {
        when(taskRepository.existsById(99)).thenReturn(false);

        taskService.delete(99);

        verify(taskRepository, never()).deleteById(any());
    }
}
