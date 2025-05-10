package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task()));

        taskService.getAllTasks();
        verify(taskRepository).findAll();
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));

        taskService.getTaskById(1L);
        verify(taskRepository).findById(1L);
    }

    @Test
    void testCreateTask() {
        Task task = new Task();

        when(taskRepository.save(task)).thenReturn(task);

        taskService.createTask(task);
        verify(taskRepository).save(task);
    }

    @Test
    void testUpdateTask() {
        Task existingTask = new Task();
        Task updatedTask = new Task("Updated", "Desc", TaskType.FEATURE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        taskService.updateTask(1L, updatedTask);
        verify(taskRepository).save(existingTask);
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }
}
