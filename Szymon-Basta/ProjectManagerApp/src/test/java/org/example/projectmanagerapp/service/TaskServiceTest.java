
package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
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
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Tasks task1 = new Tasks();
        task1.setTitle("Task 1");

        Tasks task2 = new Tasks();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a task")
    void testCreateTask() {
        Tasks task = new Tasks();
        task.setTitle("New Task");

        when(taskRepository.save(task)).thenReturn(task);

        Tasks createdTask = taskService.createTask(task);
        assertEquals("New Task", createdTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update an existing task")
    void testUpdateTask() {
        Tasks existing = new Tasks();
        existing.setId(1L);
        existing.setTitle("Old Title");

        Tasks updated = new Tasks();
        updated.setTitle("New Title");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(any(Tasks.class))).thenReturn(existing);

        Tasks result = taskService.updateTask(1L, updated);
        assertEquals("New Title", result.getTitle());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete task if exists")
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should get task by ID")
    void testGetTaskById() {
        Tasks task = new Tasks();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Tasks result = taskService.getTaskById(1L);
        assertEquals(1L, result.getId());
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw error task not found by ID")
    void testGetTaskByIdThrows() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(99L));
    }

    @Test
    @DisplayName("Should throw error deleting non-existent task")
    void testDeleteTaskThrows() {
        when(taskRepository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(99L));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent task")
    void testUpdateTaskThrows() {
        Tasks updated = new Tasks();
        updated.setTitle("ShouldFail");
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskService.updateTask(99L, updated));
    }

}
