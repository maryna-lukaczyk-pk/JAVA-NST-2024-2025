package com.example.projectmanagerapp.unit.controller;

import org.example.projectmanager.controller.TaskController;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.TaskRepository;
import org.example.projectmanager.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class TaskControllerTest {
    private TaskRepository taskRepository;
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        taskRepository = org.mockito.Mockito.mock(TaskRepository.class);
        TaskService taskService = new TaskService(taskRepository);
        taskController = new TaskController(taskService);
    }

    @Test
    public void Get_ShouldReturn_Task() throws EntityNotFoundException {
        var taskId = 1L;
        var task = new Task();
        task.setId(taskId);
        task.setTitle("Task A");

        org.mockito.Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        var result = taskController.get(taskId);

        Assertions.assertEquals(task.getId(), result.id);
        Assertions.assertEquals(task.getTitle(), result.title);
    }

    @Test
    public void Get_ShouldThrow_EntityNotFoundException() {
        var taskId = 1L;

        org.mockito.Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskController.get(taskId));
    }

    @Test
    public void Create_ShouldReturn_CreatedTaskId() {
        var task = new Task();
        task.setId(1L);
        task.setTitle("Task A");

        org.mockito.Mockito.when(taskRepository.save(org.mockito.Mockito.any(Task.class))).thenReturn(task);

        var dto = new org.example.projectmanager.dto.task.TaskCreateDto();
        dto.title = "Task A";

        var result = taskController.create(dto);

        Assertions.assertEquals(task.getId(), result);
    }

    @Test
    public void Delete_ShouldDelete_Task() throws EntityNotFoundException {
        var taskId = 1L;

        org.mockito.Mockito.when(taskRepository.existsById(taskId)).thenReturn(true);

        taskController.delete(taskId);

        org.mockito.Mockito.verify(taskRepository).deleteById(taskId);
    }

    @Test
    public void Delete_ShouldThrow_EntityNotFoundException() {
        var taskId = 1L;

        org.mockito.Mockito.when(taskRepository.existsById(taskId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskController.delete(taskId));
    }

    @Test
    public void Update_ShouldUpdate_Task() throws EntityNotFoundException {
        var taskId = 1L;
        var task = new Task();
        task.setId(taskId);
        task.setTitle("Task A");

        org.mockito.Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        var dto = new org.example.projectmanager.dto.task.TaskEditDto();
        dto.id = taskId;
        dto.title = "Task B";

        taskController.update(dto);

        Assertions.assertEquals("Task B", task.getTitle());
    }

    @Test
    public void Update_ShouldThrow_EntityNotFoundException() {
        var taskId = 1L;

        org.mockito.Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        var dto = new org.example.projectmanager.dto.task.TaskEditDto();
        dto.id = taskId;

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskController.update(dto));
    }
}
