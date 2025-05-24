package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void testGetAllTasks() {
        List<Task> tasks = List.of(new Task());
        when(taskRepository.findAll()).thenReturn(tasks);

        assertEquals(tasks, taskService.getAllTasks());
    }

    @Test
    void testGetTaskById_WhenFound() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertEquals(task, taskService.getTaskById(1L));
    }

    @Test
    void testGetTaskById_WhenNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testCreateTask() {
        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        assertEquals(task, taskService.createTask(task));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setName("Old");

        Task updated = new Task();
        updated.setName("New");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task result = taskService.updateTask(1L, updated);
        assertEquals("New", result.getName());
    }

    @Test
    void testDeleteTask_WhenExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTask_WhenNotExists() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L));
    }
}
