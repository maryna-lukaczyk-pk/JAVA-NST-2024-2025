package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
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
        Tasks task1 = new Tasks();
        task1.setTitle("Task 1");

        Tasks task2 = new Tasks();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        Tasks task = new Tasks();
        task.setTitle("Test Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Tasks result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }
}
