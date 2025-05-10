package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("getAllTasks() should return list of all tasks")
    void testGetAllTasks() {
        Task t1 = new Task(); t1.setId(1L); t1.setTitle("T1");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("T2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getTaskById() should return task when found")
    void testGetTaskByIdFound() {
        Task t = new Task(); t.setId(1L); t.setTitle("Found");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Found", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("getTaskById() should return null when not found")
    void testGetTaskByIdNotFound() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        Task result = taskService.getTaskById(999L);

        assertNull(result);
        verify(taskRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("createTask() should save and return task")
    void testCreateTask() {
        Task input = new Task(); input.setTitle("New");
        Task saved = new Task(); saved.setId(5L); saved.setTitle("New");
        when(taskRepository.save(input)).thenReturn(saved);

        Task result = taskService.createTask(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("New", result.getTitle());
        verify(taskRepository, times(1)).save(input);
    }

    @Test
    @DisplayName("updateTask() should update existing task")
    void testUpdateTaskFound() {
        Task existing = new Task(); existing.setId(10L); existing.setTitle("Old");
        Task updateData = new Task(); updateData.setId(10L); updateData.setTitle("Updated");
        when(taskRepository.findById(10L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task result = taskService.updateTask(updateData);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Updated", result.getTitle());
        // upewniamy się, że beanUtils faktycznie nadpisał name
        verify(taskRepository).findById(10L);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("updateTask() should return null when task not found")
    void testUpdateTaskNotFound() {
        Task updateData = new Task(); updateData.setId(99L); updateData.setTitle("X");
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Task result = taskService.updateTask(updateData);

        assertNull(result);
        verify(taskRepository).findById(99L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteTask() should delete and return existing task")
    void testDeleteTaskFound() {
        Task existing = new Task(); existing.setId(20L); existing.setTitle("ToDelete");
        when(taskRepository.findById(20L)).thenReturn(Optional.of(existing));
        // delete nic nie zwraca

        Task result = taskService.deleteTask(20L);

        assertNotNull(result);
        assertEquals(20L, result.getId());
        verify(taskRepository).findById(20L);
        verify(taskRepository).delete(existing);
    }

    @Test
    @DisplayName("deleteTask() should return null when task not found")
    void testDeleteTaskNotFound() {
        when(taskRepository.findById(123L)).thenReturn(Optional.empty());

        Task result = taskService.deleteTask(123L);

        assertNull(result);
        verify(taskRepository).findById(123L);
        verify(taskRepository, never()).delete(any());
    }
}
