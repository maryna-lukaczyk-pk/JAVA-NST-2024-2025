package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.task_type;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService();

        // Use reflection to set the repository
        try {
            java.lang.reflect.Field field = TaskService.class.getDeclaredField("taskRepository");
            field.setAccessible(true);
            field.set(taskService, taskRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        // Arrange
        Task task1 = new Task();
        task1.setTitle("TestTask1");

        Task task2 = new Task();
        task2.setTitle("TestTask2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // Act
        List<Task> tasks = taskService.getAllTasks();

        // Assert
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("TestTask");
        task.setDescription("Test Description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act
        Optional<Task> result = taskService.getTaskById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestTask", result.get().getTitle());
        assertEquals("Test Description", result.get().getDescription());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create task")
    void testCreateTask() {
        // Arrange
        Task task = new Task();
        task.setTitle("NewTask");
        task.setDescription("New Description");
        task.setTaskType(task_type.OK);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act
        Task result = taskService.createTask(task);

        // Assert
        assertEquals("NewTask", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(task_type.OK, result.getTaskType());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update all task fields")
    void testUpdateAllTaskFields() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskType(task_type.OK);

        Task updatedDetails = new Task();
        updatedDetails.setTitle("NewTitle");
        updatedDetails.setDescription("New Description");
        updatedDetails.setTaskType(task_type.DELAY);
        updatedDetails.setPriorityName("High");
        updatedDetails.setProject(new org.example.projectmanagerapp.entity.Project());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("NewTitle", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(task_type.DELAY, result.getTaskType());
        assertEquals("High", result.getPriorityName());
        assertNotNull(result.getProject());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should update only task title")
    void testUpdateTaskTitleOnly() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskType(task_type.OK);

        Task updatedDetails = new Task();
        updatedDetails.setTitle("NewTitle");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("NewTitle", result.getTitle());
        assertEquals("Old Description", result.getDescription());
        assertEquals(task_type.OK, result.getTaskType());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should update only task description")
    void testUpdateTaskDescriptionOnly() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskType(task_type.OK);

        Task updatedDetails = new Task();
        updatedDetails.setDescription("New Description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldTitle", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(task_type.OK, result.getTaskType());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should update only task type")
    void testUpdateTaskTypeOnly() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskType(task_type.OK);

        Task updatedDetails = new Task();
        updatedDetails.setTaskType(task_type.DELAY);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldTitle", result.getTitle());
        assertEquals("Old Description", result.getDescription());
        assertEquals(task_type.DELAY, result.getTaskType());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should update only task project")
    void testUpdateTaskProjectOnly() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskType(task_type.OK);

        Task updatedDetails = new Task();
        updatedDetails.setProject(new org.example.projectmanagerapp.entity.Project());

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldTitle", result.getTitle());
        assertEquals("Old Description", result.getDescription());
        assertEquals(task_type.OK, result.getTaskType());
        assertNotNull(result.getProject());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should update only task priority name")
    void testUpdateTaskPriorityNameOnly() {
        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskType(task_type.OK);

        Task updatedDetails = new Task();
        updatedDetails.setPriorityName("High");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldTitle", result.getTitle());
        assertEquals("Old Description", result.getDescription());
        assertEquals(task_type.OK, result.getTaskType());
        assertEquals("High", result.getPriorityName());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should return null when updating non-existent task")
    void testUpdateNonExistentTask() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Task result = taskService.updateTask(1L, new Task());

        // Assert
        assertNull(result);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Should delete task")
    void testDeleteTask() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = taskService.deleteTask(1L);

        // Assert
        assertTrue(result);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent task")
    void testDeleteNonExistentTask() {
        // Arrange
        when(taskRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = taskService.deleteTask(1L);

        // Assert
        assertFalse(result);
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, never()).deleteById(any());
    }
}
