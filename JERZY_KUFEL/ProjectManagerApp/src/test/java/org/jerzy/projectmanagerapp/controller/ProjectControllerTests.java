package org.jerzy.projectmanagerapp.controller;

import org.jerzy.projectmanagerapp.entity.Task;
import org.jerzy.projectmanagerapp.repository.TaskRepository;
import org.jerzy.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskControllerTest {

  private TaskRepository taskRepository;
  private TaskService taskService;
  private TaskController taskController;

  @BeforeEach
  void setUp() {
    taskRepository = Mockito.mock(TaskRepository.class);
    taskService = new TaskService(taskRepository);
  }

  @Test
  void testGetAllTasks() {
    when(taskService.getAllTasks()).thenReturn(List.of(new Task(), new Task()));

    List<Task> result = taskController.get();
    assertEquals(2, result.size());
  }

  @Test
  void testGetTaskById() {
    Task task = new Task();
    task.setId(1L);
    when(taskService.getById("1")).thenReturn(task);

    Task result = taskController.getTaskById("1");
    assertEquals(1L, result.getId());
  }

  @Test
  void testPost() {
    Task task = new Task();
    task.setTitle("New Task");
    when(taskService.create(any())).thenReturn(task);

    ResponseEntity<Task> response = taskController.post(task);
    assertEquals(201, response.getStatusCode());
    assertEquals("New Task", response.getBody().getTitle());
  }

  @Test
  void testUpdateTask_valid() {
    Task task = new Task();
    task.setTitle("Updated");
    when(taskService.update("1", task)).thenReturn(task);

    ResponseEntity<Task> response = taskController.updateProject("1", task);
    assertEquals(200, response.getStatusCode());
    assertEquals("Updated", response.getBody().getTitle());
  }

  @Test
  void testUpdateTask_invalid() {
    when(taskService.update(eq("1"), any())).thenThrow(new IllegalArgumentException());

    ResponseEntity<Task> response = taskController.updateProject("1", new Task());
    assertEquals(404, response.getStatusCode());
  }

  @Test
  void testDeleteTask_valid() {
    ResponseEntity<Void> response = taskController.deleteProject("1");
    assertEquals(204, response.getStatusCode());
    verify(taskService).delete("1");
  }

  @Test
  void testDeleteTask_invalid() {
    doThrow(new IllegalArgumentException()).when(taskService).delete("1");

    ResponseEntity<Void> response = taskController.deleteProject("1");
    assertEquals(404, response.getStatusCode());
  }
}
