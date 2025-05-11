package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getAllTasks_returnsListOfTasks() {

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test task");
        List<Task> tasks = Collections.singletonList(task);
        given(taskService.getAll()).willReturn(tasks);


        List<Task> result = taskController.getAllTasks();


        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Test task", result.get(0).getTitle());
        verify(taskService).getAll();
    }

    @Test
    void getTaskById_returnsTask() {

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task 1");
        given(taskService.getById(1)).willReturn(task);


        Task result = taskController.getTaskById(1);


        assertEquals(1, result.getId());
        assertEquals("Test Task 1", result.getTitle());
        verify(taskService).getById(1);
    }

    @Test
    void createTask_returnsCreatedTask() {

        Task inputTask = new Task();
        inputTask.setTitle("New Task");

        Task savedTask = new Task();
        savedTask.setId(1);
        savedTask.setTitle("New Task");

        given(taskService.create(any(Task.class))).willReturn(savedTask);


        Task result = taskController.createTask(inputTask);


        assertEquals(1, result.getId());
        assertEquals("New Task", result.getTitle());
        verify(taskService).create(inputTask);
    }

    @Test
    void updateTask_returnsUpdatedTask() {

        Task inputTask = new Task();
        inputTask.setTitle("Updated Task");

        Task updatedTask = new Task();
        updatedTask.setId(1);
        updatedTask.setTitle("Updated Task");

        given(taskService.update(eq(1), any(Task.class))).willReturn(updatedTask);

        // when
        Task result = taskController.updateTask(1, inputTask);


        assertEquals(1, result.getId());
        assertEquals("Updated Task", result.getTitle());
        verify(taskService).update(1, inputTask);
    }

    @Test
    void deleteTask_returnsNoContent() {

        doNothing().when(taskService).delete(1);


        ResponseEntity<Void> result = taskController.deleteTask(1);


        assertEquals(204, result.getStatusCodeValue());
        assertNull(result.getBody());
        verify(taskService).delete(1);
    }
}