package com.example.projectmanagerapp.service;


import com.example.projectmanagerapp.entity.tasks;
import com.example.projectmanagerapp.repository.tasks_repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class tasks_test {

    @Mock
    private tasks_repository tasks_repository;

    @InjectMocks
    private tasks_service tasks_service;

    private tasks task1;
    private tasks task2;

    @BeforeEach
    void setUp() {
        task1 = new tasks();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setPriority("HIGH");
        task1.setProject_id(1L);

        task2 = new tasks();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setPriority("MEDIUM");
        task2.setProject_id(1L);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        when(tasks_repository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<tasks> result = tasks_service.getAllTasks();

        assertEquals(2, result.size());
        verify(tasks_repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a task")
    void testCreateTask() {
        when(tasks_repository.save(any(tasks.class))).thenReturn(task1);

        tasks result = tasks_service.create_task(task1);

        assertNotNull(result);
        assertEquals(task1.getTitle(), result.getTitle());
        assertEquals(task1.getDescription(), result.getDescription());
        verify(tasks_repository, times(1)).save(any(tasks.class));
    }

    @Test
    @DisplayName("Should find task by ID")
    void testGetTaskById() {
        when(tasks_repository.findById(1L)).thenReturn(Optional.of(task1));

        Optional<tasks> result = tasks_service.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals(task1.getTitle(), result.get().getTitle());
        verify(tasks_repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when task not found")
    void testGetTaskByIdNotFound() {
        when(tasks_repository.findById(3L)).thenReturn(Optional.empty());

        Optional<tasks> result = tasks_service.getTaskById(3L);

        assertFalse(result.isPresent());
        verify(tasks_repository, times(1)).findById(3L);
    }



    @Test
    @DisplayName("Should update task")
    void testUpdateTask() {
        Long taskId = 1L;
        tasks updatedTask = new tasks();
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setPriority("LOW");
        updatedTask.setProject_id(2L);

        when(tasks_repository.findById(taskId)).thenReturn(Optional.of(task1));
        when(tasks_repository.save(any(tasks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tasks result = tasks_service.updateTask(taskId, updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("LOW", result.getPriority());
        assertEquals(2L, result.getProject_id());
        verify(tasks_repository, times(1)).findById(taskId);
        verify(tasks_repository, times(1)).save(any(tasks.class));
    }


}