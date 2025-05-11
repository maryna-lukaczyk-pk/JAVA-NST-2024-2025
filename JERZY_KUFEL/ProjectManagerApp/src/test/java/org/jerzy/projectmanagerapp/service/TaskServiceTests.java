package org.jerzy.projectmanagerapp.service;

import org.jerzy.projectmanagerapp.entity.Task;
import org.jerzy.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
  void testGetAllTasks() {
    List<Task> tasks = List.of(new Task(), new Task());
    when(taskRepository.findAll()).thenReturn(tasks);

    List<Task> result = taskService.getAllTasks();
    assertEquals(2, result.size());
    verify(taskRepository).findAll();
  }

  @Test
  void testGetById_validId() {
    Task task = new Task();
    task.setId(1L);
    when(taskRepository.findById(1)).thenReturn(Optional.of(task));

    Task result = taskService.getById("1");
    assertEquals(1L, result.getId());
  }

  @Test
  void testGetById_invalidId_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> taskService.getById("abc"));
  }

  @Test
  void testCreateTask() {
    Task task = new Task();
    when(taskRepository.save(task)).thenReturn(task);

    Task result = taskService.create(task);
    assertEquals(task, result);
  }

  @Test
  void testUpdate_validId() {
    Task existing = new Task();
    existing.setId(1L);
    existing.setTitle("Old");

    Task updated = new Task();
    updated.setTitle("New");

    when(taskRepository.findById(1)).thenReturn(Optional.of(existing));
    when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Task result = taskService.update("1", updated);
    assertEquals("New", result.getTitle());
  }

  @Test
  void testUpdate_invalidId_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> taskService.update("abc", new Task()));
  }

  @Test
  void testDelete_validId() {
    when(taskRepository.existsById(1)).thenReturn(true);

    taskService.delete("1");

    verify(taskRepository).deleteById(1);
  }

  @Test
  void testDelete_nonExistingId_throwsException() {
    when(taskRepository.existsById(99)).thenReturn(false);
    assertThrows(IllegalArgumentException.class, () -> taskService.delete("99"));
  }

  @Test
  void testDelete_invalidIdFormat_throwsException() {
    assertThrows(IllegalArgumentException.class, () -> taskService.delete("xyz"));
  }
}
