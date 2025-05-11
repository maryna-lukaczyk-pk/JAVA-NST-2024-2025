package org.example.projectmanagerapp;
import org.example.projectmanagerapp.controller.TaskController;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class TaskControllerTest {
    private TaskController taskController;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskService = Mockito.mock(TaskService.class);
        taskController = new TaskController(taskService);
    }

    @Test
    @DisplayName("GET /api/tasks - Should return all tasks")
    public void shouldReturnAllTasks() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        // When
        List<Task> result = taskController.getAllTasks();

        // Then
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Task 2", result.get(1).getTitle());
        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - Should return task by ID")
    public void shouldReturnTaskById() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        when(taskService.findTaskById(1L)).thenReturn(task);

        // When
        Task result = taskController.getTaskById(1L);

        // Then
        assertEquals("Test Task", result.getTitle());
        verify(taskService, times(1)).findTaskById(1L);
    }

    @Test
    @DisplayName("POST /api/tasks - Should create new task")
    public void shouldCreateTask() {
        // Given
        Task inputTask = new Task();
        inputTask.setTitle("New Task");

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");

        when(taskService.createTask(inputTask)).thenReturn(savedTask);

        // When
        Task result = taskController.createTask(inputTask);

        // Then
        assertEquals(1L, result.getId());
        assertEquals("New Task", result.getTitle());
        verify(taskService, times(1)).createTask(inputTask);
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - Should delete task by ID")
    public void shouldDeleteTaskById() {
        // Given
        doNothing().when(taskService).deleteTask(1L);

        // When
        taskController.deleteTask(1L);

        // Then
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @DisplayName("PUT /api/task/{id} - Should update task")
    public void shouldUpdateTask() {
        // Given
        Long taskId = 1L;
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        doNothing().when(taskService).updateTask(taskId, updatedTask);

        // When
        taskController.updateTask(taskId, updatedTask);

        // Then
        verify(taskService, times(1)).updateTask(taskId, updatedTask);
    }
}
