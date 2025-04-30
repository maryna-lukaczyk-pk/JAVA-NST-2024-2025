package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() throws Exception {
        taskService = Mockito.mock(TaskService.class);
        taskController = new TaskController(taskService);

        // Although dependency injection happens via constructor, this ensures the field is set.
        java.lang.reflect.Field taskServiceField = TaskController.class.getDeclaredField("taskService");
        taskServiceField.setAccessible(true);
        taskServiceField.set(taskController, taskService);
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setId(1);
        task1.setTitle("Task 1");
        task1.setTaskType(TaskType.FEATURE);

        Task task2 = new Task();
        task2.setId(2);
        task2.setTitle("Task 2");
        task2.setTaskType(TaskType.BUG);

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskController.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void testGetTaskByIdExists() {
        Task task = new Task();
        task.setId(1);
        task.setTitle("Existing Task");
        task.setTaskType(TaskType.DOCUMENTATION);

        when(taskService.getTaskById(1)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTaskById(1);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Existing Task", Objects.requireNonNull(response.getBody()).getTitle());

        verify(taskService, times(1)).getTaskById(1);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskService.getTaskById(100)).thenReturn(null);

        ResponseEntity<Task> response = taskController.getTaskById(100);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(taskService, times(1)).getTaskById(100);
    }

    @Test
    void testCreateTask() {
        Task inputTask = new Task();
        inputTask.setTitle("New Task");
        inputTask.setTaskType(TaskType.FEATURE);

        Task createdTask = new Task();
        createdTask.setId(1);
        createdTask.setTitle("New Task");
        createdTask.setTaskType(TaskType.FEATURE);

        when(taskService.createTask(inputTask)).thenReturn(createdTask);

        Task result = taskController.createTask(inputTask);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New Task", result.getTitle());

        verify(taskService, times(1)).createTask(inputTask);
    }

    @Test
    void testUpdateTask() {
        Task updatedData = new Task();
        updatedData.setTitle("Updated Task");
        updatedData.setTaskType(TaskType.BUG);

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("Updated Task");
        updatedTask.setTaskType(TaskType.BUG);

        when(taskService.updateTask(1, updatedData)).thenReturn(updatedTask);

        Task result = taskController.updateTask(1, updatedData);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Updated Task", result.getTitle());

        verify(taskService, times(1)).updateTask(1, updatedData);
    }

    @Test
    void testDeleteTask() {
        taskController.deleteTask(1);
        verify(taskService, times(1)).deleteTask(1);
    }
}