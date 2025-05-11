package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Zwraca wszystkie taski")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task1");

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Tworzy nowy task")
    void testCreateTask() {
        Task taskToCreate = new Task();
        taskToCreate.setTitle("NewTask");

        Task savedTask = new Task();
        savedTask.setId(1);
        savedTask.setTitle("NewTask");

        when(taskRepository.save(taskToCreate)).thenReturn(savedTask);

        Task result = taskService.createTask(taskToCreate);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("NewTask", result.getTitle());
        verify(taskRepository, times(1)).save(taskToCreate);
    }

    @Test
    @DisplayName("Zwraca tak po id")
    void testGetTaskByIdExists() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("TestTask");

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1);

        assertTrue(result.isPresent());
        assertEquals("TestTask", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Zwraca None kiedy nie istnieje po id")
    void testGetTaskByIdNotExists() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.getTaskById(1);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Usuwa task po id")
    void testDeleteTaskById() {
        taskService.deleteTaskById(1);

        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Updateuje task jezeli istnieje")
    void testUpdateTaskExists() {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("OldTitle");

        Task taskDetails = new Task();
        taskDetails.setTitle("NewTitle");

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("NewTitle");

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1, taskDetails);

        assertNotNull(result);
        assertEquals("NewTitle", result.getTitle());
        verify(taskRepository, times(1)).findById(1);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Zwraca null przy updatecie nieistniejacego tasku")
    void testUpdateTaskNotExists() {
        Task taskDetails = new Task();
        taskDetails.setTitle("NewTitle");

        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        Task result = taskService.updateTask(1, taskDetails);

        assertNull(result);
        verify(taskRepository, times(1)).findById(1);
        verify(taskRepository, never()).save(any(Task.class));
    }
}