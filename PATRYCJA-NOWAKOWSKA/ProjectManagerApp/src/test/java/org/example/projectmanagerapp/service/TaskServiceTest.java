package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    @DisplayName("Powinno zwrócić wszystkie zadania")
    void shouldReturnAllTasks() {
        Task task1 = new Task();
        task1.setName("Task1");
        Task task2 = new Task();
        task2.setName("Task2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinno utworzyć nowe zadanie")
    void shouldCreateTask() {
        Task task = new Task();
        task.setName("NewTask");

        when(taskRepository.save(task)).thenReturn(task);

        Task created = taskService.createTask(task);

        assertNotNull(created);
        assertEquals("NewTask", created.getName());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Powinno zwrócić zadanie po ID")
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setName("TestTask");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> found = taskService.getTaskById(1L);

        assertTrue(found.isPresent());
        assertEquals("TestTask", found.get().getName());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinno zaktualizować zadanie")
    void shouldUpdateTask() {
        Task existingTask = new Task();
        existingTask.setName("OldTask");
        Task updatedTask = new Task();
        updatedTask.setName("NewTask");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("NewTask", result.getName());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existingTask);
    }

    @Test
    @DisplayName("Powinno rzucić wyjątek, gdy zadanie do aktualizacji nie istnieje")
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                taskService.updateTask(1L, new Task()));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    @DisplayName("Powinno usunąć zadanie po ID")
    void shouldDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }
}
