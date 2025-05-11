package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.example.projectmanagerapp.entity.TaskType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    public void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task1");

        Task task2 = new Task();
        task2.setTitle("Task2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAll();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    public void testGetByID() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("TestTask");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> result = taskService.getByID(1L);

        assertTrue(result.isPresent());
        assertEquals("TestTask", result.get().getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    public void testCreateTask() {
        Task newTask = new Task();
        newTask.setTitle("NewTask");

        when(taskRepository.save(newTask)).thenReturn(newTask);

        Task createdTask = taskService.create(newTask);

        assertNotNull(createdTask);
        assertEquals("NewTask", createdTask.getTitle());
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    @DisplayName("Should update an existing task")
    public void testUpdateTask() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("ExistingTask");

        Task updatedTask = new Task();
        updatedTask.setTitle("UpdatedTask");
        updatedTask.setDescription("UpdatedDescription");
        updatedTask.setTask_type(TaskType.BUG);
        updatedTask.setProject(null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Optional<Task> result = taskService.update(1L, updatedTask);

        assertTrue(result.isPresent());
        assertEquals("UpdatedTask", result.get().getTitle());
        assertEquals("UpdatedDescription", result.get().getDescription());
        assertEquals(TaskType.BUG, result.get().getTask_type());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should delete an existing task")
    public void testDeleteTask() {
        taskService.delete(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
