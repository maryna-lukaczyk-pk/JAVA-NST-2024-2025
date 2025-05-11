package org.example.projectmanagerapp;
import org.example.projectmanagerapp.services.TaskService;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Tasks task1 = new Tasks();
        task1.setTitle("Task one");

        Tasks task2 = new Tasks();
        task2.setTitle("Task two");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should return an empty list of tasks")
    void testGetAllTasksEmpty() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<Tasks> tasks = taskService.getAllTasks();
        assertEquals(0, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() {
        Tasks task = new Tasks();
        task.setTitle("NewTask");

        when(taskRepository.save(task)).thenReturn(task);

        Tasks created = taskService.createTask(task);
        assertEquals("NewTask", created.getTitle());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should update an existing task")
    void testUpdateTask() {
        long id = 1L;
        Tasks existing = new Tasks();
        existing.setTitle("OldTask");
        existing.setDescription("OldDescription");

        Tasks updated = new Tasks();
        updated.setTitle("UpdatedTask");
        updated.setDescription("NewDescription");

        when(taskRepository.findById(id)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Tasks result = taskService.updateTask(id, updated);
        assertEquals("UpdatedTask", result.getTitle());
        assertEquals("NewDescription", result.getDescription());
        verify(taskRepository).findById(id);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete a task by ID")
    void testDeleteTask() {
        long id = 2L;

        doNothing().when(taskRepository).deleteById(id);

        taskService.deleteTask(id);
        verify(taskRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw when task not found on Update")
    void testUpdateTaskNotFound() {
        long id = 3L;
        Tasks updated = new Tasks();
        updated.setTitle("Updated");

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(id, updated);
        });

        assertEquals("Task not found", ex.getMessage());
        verify(taskRepository).findById(id);
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        long id = 4L;
        Tasks task = new Tasks();
        task.setTitle("SampleTask");

        when(taskRepository.findById(id)).thenReturn(Optional.of(task));

        Tasks result = taskService.getTaskById(id);
        assertEquals("SampleTask", result.getTitle());
        verify(taskRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw when task not found by ID")
    void testGetTaskByIdNotFound() {
        long id = 5L;

        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(id);
        });

        assertEquals("Task not found", ex.getMessage());
        verify(taskRepository).findById(id);
    }
}