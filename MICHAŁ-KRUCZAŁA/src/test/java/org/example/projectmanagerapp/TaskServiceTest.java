package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repositories.TaskRepository;
import org.example.projectmanagerapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
        task1.setTitle("Task1");

        Task task2 = new Task();
        task2.setTitle("Task2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create task")
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("NewTask");

        when(taskRepository.save(task)).thenReturn(task);

        Task created = taskService.createTask(task);

        assertNotNull(created);
        assertEquals("NewTask", created.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should find task by ID")
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Task1");

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Optional<Task> found = taskService.getTaskById(1);

        assertTrue(found.isPresent());
        assertEquals("Task1", found.get().getTitle());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTask() {
        taskService.deleteTask(1);

        verify(taskRepository, times(1)).deleteById(1);
    }
}