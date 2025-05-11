package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAll();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("My Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("My Task", result.get().getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("New Task");

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.createTask(task);

        assertEquals("New Task", savedTask.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateExistingTask() {
        Task existing = new Task();
        existing.setId(1L);
        existing.setTitle("Old Title");

        Task updated = new Task();
        updated.setTitle("Updated Title");
        updated.setDescription("New Description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(i -> i.getArgument(0));

        Task result = taskService.updateTask(1L, updated);

        assertEquals("Updated Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("Should save new task when updating non-existing one")
    void testUpdateNonExistingTask() {
        Task task = new Task();
        task.setTitle("Example Task");

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.updateTask(99L, task);

        assertEquals("Example Task", result.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTask() {
        taskService.deleteTask(3L);
        verify(taskRepository).deleteById(3L);
    }
}
