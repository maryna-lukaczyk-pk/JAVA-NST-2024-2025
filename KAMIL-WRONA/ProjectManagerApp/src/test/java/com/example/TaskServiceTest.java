package com.example;


import com.example.entity.Task;
import com.example.repository.TaskRepository;
import com.example.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    public void testGetAllTasks() {

        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        assertEquals("Task 2", tasks.get(1).getTitle());

        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    public void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("TestTask");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getTaskById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestTask", result.get().getTitle());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create new task")
    public void testCreateTask() {
        Task task = new Task();
        task.setTitle("NewTask");

        when(taskRepository.save(task)).thenReturn(task);

        Task savedTask = taskService.createTask(task);

        assertEquals("NewTask", savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update existing task")
    public void testUpdateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("OldTitle");
        task.setDescription("OldDesc");

        Task updatedTask = new Task();
        updatedTask.setTitle("NewTitle");
        updatedTask.setDescription("NewDesc");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Optional<Task> result = taskService.updateTask(1L, updatedTask);

        assertTrue(result.isPresent());
        assertEquals("NewTitle", result.get().getTitle());
        assertEquals("NewDesc", result.get().getDescription());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should delete task by ID")
    public void testDeleteTask() {
        Long taskId = 1L;

        doNothing().when(taskRepository).deleteById(taskId);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Should return empty when task not found for update")
    public void testUpdateTask_NotFound() {
        Task updatedTask = new Task();
        updatedTask.setTitle("NewTitle");
        updatedTask.setDescription("NewDesc");

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.updateTask(999L, updatedTask);

        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(999L);
    }


}
