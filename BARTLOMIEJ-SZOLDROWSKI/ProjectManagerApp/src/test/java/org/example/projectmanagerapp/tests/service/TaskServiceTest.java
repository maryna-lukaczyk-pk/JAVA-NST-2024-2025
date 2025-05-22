package org.example.projectmanagerapp.tests.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void createTask_ShouldSaveAndReturnTask() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        Task createdTask = taskService.createTask(task);

        // Then
        assertThat(createdTask.getTitle()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void getTaskById_WhenExists_ShouldReturnTask() {
        // Given
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // When
        Optional<Task> foundTask = taskService.getTaskById(1L);

        // Then
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getId()).isEqualTo(1L);
    }

    @Test
    void updateTask_ShouldUpdateFields() {
        // Given
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");

        Task updateData = new Task();
        updateData.setTitle("New Title");
        updateData.setDescription("Updated Description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Task updatedTask = taskService.updateTask(1L, updateData);

        // Then
        assertThat(updatedTask.getTitle()).isEqualTo("New Title");
        assertThat(updatedTask.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void deleteTask_ShouldInvokeRepositoryDelete() {
        // Given
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void getAllTasks_ShouldReturnTaskList() {
        // Given
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task()));

        // When
        List<Task> tasks = taskService.getAllTasks();

        // Then
        assertThat(tasks).hasSize(2);
    }
}