// src/test/java/com/example/projectmanagerapp/service/TaskServiceTest.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService    = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task t1 = new Task(); t1.setTitle("T1");
        Task t2 = new Task(); t2.setTitle("T2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> tasks = taskService.getTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find task by ID")
    void testGetTaskById() {
        Task t = new Task(); t.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        Task result = taskService.getTaskById(1L);

        assertEquals(1L, result.getId());
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create task")
    void testCreateTask() {
        Task t = new Task(); t.setTitle("New");
        when(taskRepository.save(t)).thenReturn(t);

        Task result = taskService.createTask(t);

        assertEquals("New", result.getTitle());
        verify(taskRepository).save(t);
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask() {
        Task existing = new Task(); existing.setId(1L);
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Task toUpdate = new Task(); toUpdate.setTitle("Upd");
        Task result   = taskService.updateTask(1L, toUpdate);

        assertEquals("Upd", result.getTitle());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }
    @Test
    @DisplayName("Should delete task")
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test @DisplayName("getTaskById() – not found")
    void testGetTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> taskService.getTaskById(99L));
    }

    @Test @DisplayName("updateTask() – not found")
    void testUpdateTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> taskService.updateTask(99L, new Task()));
    }
}
