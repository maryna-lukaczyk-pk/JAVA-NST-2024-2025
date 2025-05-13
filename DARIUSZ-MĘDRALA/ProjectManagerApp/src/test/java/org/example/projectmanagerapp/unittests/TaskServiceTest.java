package org.example.projectmanagerapp.unittests;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.services.ProjectService;
import org.example.projectmanagerapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    private ProjectRepository projectRepository;

    @BeforeEach
    public void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskService = new TaskService(taskRepository, projectRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void findAll_shouldReturnAllTasks() {

        Task task1 = new Task();
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setTitle("Task 2");

        List<Task> expectedTasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> actualTasks = taskService.findAll();

        assertEquals(expectedTasks.size(), actualTasks.size());
        assertTrue(actualTasks.contains(task1));
        assertTrue(actualTasks.contains(task2));
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new task")
    void createTask_shouldCreateNewTask() {

        Task task = new Task();
        task.setTitle("Task 1");

        when(taskRepository.save(task)).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertEquals(task, createdTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should delete a task")
    void deleteTask_shouldDeleteTask() {

        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task deletedTask = taskService.deleteTask(1L);

        assertEquals(task, deletedTask);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Should update a task")
    void updateTask_shouldUpdateTask() {

        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(updatedTask)).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals(updatedTask, result);
        verify(taskRepository, times(1)).save(updatedTask);
    }

    @Test
    @DisplayName("Should find a task by ID")
    void findById_shouldReturnTask() {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.findById(1L);

        assertEquals(task, foundTask);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found")
    void findById_shouldThrowException() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            taskService.findById(1L);
        } catch (RuntimeException e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found on delete")
    void deleteTask_shouldThrowException() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            taskService.deleteTask(1L);
        } catch (RuntimeException e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found on update")
    void updateTask_shouldThrowException() {

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            taskService.updateTask(1L, updatedTask);
        } catch (RuntimeException e) {
            assertEquals("Task not found", e.getMessage());
        }

        verify(taskRepository, times(1)).findById(1L);
    }
}
