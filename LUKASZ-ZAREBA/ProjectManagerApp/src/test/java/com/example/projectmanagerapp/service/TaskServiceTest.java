package com.example.projectmanagerapp.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getAll_ReturnsAllTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        List<Task> mockTasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(mockTasks);

        List<Task> result = taskService.getAll();

        assertEquals(mockTasks, result);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingTask_ReturnsTask() {
        Task task = new Task();
        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.getById(1);

        assertEquals(task, result);
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void getById_NonExistingTask_ThrowsException() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.getById(1));
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    void create_Task_SavesAndReturnsTask() {
        Task newTask = new Task();
        Task savedTask = new Task();
        when(taskRepository.save(newTask)).thenReturn(savedTask);

        Task result = taskService.create(newTask);

        assertEquals(savedTask, result);
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    void update_ExistingTask_UpdatesAndReturnsTask() {
        Task updates = new Task();
        Task updatedTask = new Task();
        updatedTask.setId(1);
        when(taskRepository.existsById(1)).thenReturn(true);
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.update(1, updates);

        assertEquals(updatedTask, result);
        assertEquals(1, result.getId());
        verify(taskRepository, times(1)).existsById(1);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void update_NonExistingTask_ThrowsException() {
        when(taskRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> taskService.update(1, new Task()));
        verify(taskRepository, times(1)).existsById(1);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void delete_ExistingTask_DeletesTask() {
        when(taskRepository.existsById(1)).thenReturn(true);

        taskService.delete(1);

        verify(taskRepository, times(1)).existsById(1);
        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_NonExistingTask_ThrowsException() {
        when(taskRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> taskService.delete(1));
        verify(taskRepository, times(1)).existsById(1);
        verify(taskRepository, never()).deleteById(anyInt());
    }
}
