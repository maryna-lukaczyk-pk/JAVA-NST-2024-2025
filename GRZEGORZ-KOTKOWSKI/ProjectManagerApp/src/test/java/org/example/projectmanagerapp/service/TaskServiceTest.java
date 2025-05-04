package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;

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
    @DisplayName("Should return task by ID when it exists")
    void testGetTaskById_WhenTaskExists() {
        Long taskId = 1L;
        Tasks existingTask = new Tasks();
        existingTask.setId(taskId);
        existingTask.setTitle("Existing Task");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        Tasks result = taskService.getTaskById(taskId);

        assertNotNull(result);
        assertEquals("Existing Task", result.getTitle());
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when task does not exist")
    void testGetTaskById_WhenTaskDoesNotExist() {
        Long taskId = 999L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> taskService.getTaskById(taskId));

        assertTrue(exception.getMessage().contains("Task with ID " + taskId + " not found."));
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Should update existing task")
    void testUpdateTask_WhenTaskExists() {
        Long taskId = 1L;
        Tasks existingTask = new Tasks();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Title");

        Tasks updatedData = new Tasks();
        updatedData.setTitle("New Title");
        updatedData.setDescription("New Description");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Tasks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tasks result = taskService.updateTask(taskId, updatedData);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when updating non-existing task")
    void testUpdateTask_WhenTaskDoesNotExist() {
        Long taskId = 999L;
        Tasks updatedData = new Tasks();
        updatedData.setTitle("New Title");

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> taskService.updateTask(taskId, updatedData));

        assertTrue(exception.getMessage().contains("Task with ID " + taskId + " not found."));
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Should delete task when it exists")
    void testDeleteTask_WhenTaskExists() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        taskService.deleteTask(taskId);

        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when task does not exist on delete")
    void testDeleteTask_WhenTaskDoesNotExist() {
        Long taskId = 999L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> taskService.deleteTask(taskId));

        assertTrue(exception.getMessage().contains("Task with ID " + taskId + " does not exist."));
        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }
}