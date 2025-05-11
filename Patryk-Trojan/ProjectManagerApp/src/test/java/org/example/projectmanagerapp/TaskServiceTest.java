package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    private TaskService taskService;
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    public void shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    public void shouldReturnTaskById() {
        Task task = new Task();
        task.setTitle("Test Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        Task result = taskService.findTaskById(1L);

        assertEquals("Test Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete task by ID")
    public void shouldDeleteTaskById() {
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    public void shouldThrowWhenUpdatingNonExistentTask() {
        Long invalidId = 99L;
        Task updateData = new Task();
        updateData.setTitle("Updated Title");

        when(taskRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(invalidId, updateData);
        });

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create a task")
    public void shouldCreateTask() {
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test Task");

        Task inputTask = new Task();
        inputTask.setTitle("Test Task");

        when(taskRepository.save(inputTask)).thenReturn(savedTask);
        when(taskRepository.findAll()).thenReturn(List.of(savedTask));

        taskService.createTask(inputTask);
        List<Task> tasks = taskService.getAllTasks();

        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).save(inputTask);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should update a task")
    public void shouldUpdateTask() {
        Long taskId = 1L;

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");

        Task inputTask = new Task();
        inputTask.setTitle("New Title");

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("New Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(updatedTask);

        taskService.updateTask(taskId, inputTask);

        assertEquals("New Title", existingTask.getTitle());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(existingTask);
    }
}
