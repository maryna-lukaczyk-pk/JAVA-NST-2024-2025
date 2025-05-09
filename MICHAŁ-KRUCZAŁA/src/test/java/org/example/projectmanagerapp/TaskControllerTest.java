package org.example.projectmanagerapp;

import org.example.projectmanagerapp.controllers.TaskController;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task1");

        Task task2 = new Task();
        task2.setTitle("Task2");

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskController.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("TestTask");

        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));

        ResponseEntity<Task> response = taskController.getTaskById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when task not found")
    void testGetTaskByIdNotFound() {
        when(taskService.getTaskById(999)).thenReturn(Optional.empty());

        ResponseEntity<Task> response = taskController.getTaskById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should create new task")
    void testCreateTask() {
        Task task = new Task();
        task.setTitle("NewTask");

        when(taskService.createTask(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(task);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).createTask(task);
    }

    @Test
    @DisplayName("Should update task")
    void testUpdateTask() {
        Task task = new Task();
        task.setTitle("UpdatedTask");

        when(taskService.updateTask(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateTask(1, task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
        verify(taskService, times(1)).updateTask(task);
    }

    @Test
    @DisplayName("Should delete task")
    void testDeleteTask() {
        ResponseEntity<Void> response = taskController.deleteTask(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).deleteTask(1);
    }
}