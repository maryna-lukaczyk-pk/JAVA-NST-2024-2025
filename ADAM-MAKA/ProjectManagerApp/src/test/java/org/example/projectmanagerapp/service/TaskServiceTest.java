package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Test getAllTasks returns list of tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1);
        Task task2 = new Task();
        task2.setId(2);
        List<Task> tasks = Arrays.asList(task1, task2);

        Mockito.when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();
        assertEquals(2, result.size());
        Mockito.verify(taskRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Test getTaskById returns task when found")
    void testGetTaskByIdFound() {
        Task task = new Task();
        task.setId(1);

        Mockito.when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Test getTaskById returns null when task not found")
    void testGetTaskByIdNotFound() {
        Mockito.when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Task result = taskService.getTaskById(1);
        assertNull(result);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Test createTask saves and returns the task")
    void testCreateTask() {
        Task task = new Task();
        task.setId(1);

        Mockito.when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.createTask(task);
        assertEquals(task.getId(), result.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    @DisplayName("Test updateTask updates and returns task when found")
    void testUpdateTaskFound() {
        Task existingTask = new Task();
        existingTask.setId(1);

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("Updated Title");

        Mockito.when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        Mockito.when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        Task result = taskService.updateTask(1, updatedTask);
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1);
        Mockito.verify(taskRepository, Mockito.times(1)).save(updatedTask);
    }

    @Test
    @DisplayName("Test updateTask returns null when task not found")
    void testUpdateTaskNotFound() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Title");

        Mockito.when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Task result = taskService.updateTask(1, updatedTask);
        assertNull(result);
        Mockito.verify(taskRepository, Mockito.times(1)).findById(1);
    }

    @Test
    @DisplayName("Test deleteTask calls deleteById on repository")
    void testDeleteTask() {
        taskService.deleteTask(1);
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(1);
    }
}