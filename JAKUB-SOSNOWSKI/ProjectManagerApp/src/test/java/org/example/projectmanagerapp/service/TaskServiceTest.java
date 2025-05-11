package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(new Task(), new Task()));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
    }

    @Test
    void getTaskById_shouldReturnTask() {
        Task task = new Task();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
    }

    @Test
    void createTask_shouldSaveTask() {
        Task task = new Task();
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);

        assertEquals(task, result);
    }

    @Test
    void updateTask_shouldUpdateFields() {
        Task oldTask = new Task();
        oldTask.setName("Old");

        Task newTask = new Task();
        newTask.setName("New");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(oldTask));
        when(taskRepository.save(any(Task.class))).thenReturn(newTask);

        Task result = taskService.updateTask(1L, newTask);

        assertEquals("New", result.getName());
    }

    @Test
    void deleteTask_shouldDeleteById() {
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }
}
