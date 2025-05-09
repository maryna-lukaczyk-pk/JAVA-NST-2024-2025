package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
    void getAllTasks() {
        Project project = new Project("P1", "D1", "ACTIVE");

        Task t1 = new Task("Task 1", "Desc 1", TaskType.HIGH_PRIORITY, project);
        Task t2 = new Task("Task 2", "Desc 2", TaskType.MEDIUM_PRIORITY, project);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void getTaskById() {
        Project p = new Project("P", "D", "S");

        Task t = new Task("T", "Details", TaskType.LOW_PRIORITY, p);
        t.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        Task result = taskService.getTaskById(1L);

        assertEquals("T", result.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create task")
    void createTask() {
        Project p = new Project("P", "D", "S");
        Task t = new Task("T", "D", TaskType.HIGH_PRIORITY, p);

        when(taskRepository.save(t)).thenReturn(t);

        Task result = taskService.createTask(t);

        assertEquals("T", result.getTitle());
        verify(taskRepository).save(t);
    }

    @Test
    @DisplayName("Should update task")
    void updateTask() {
        Project p = new Project("P", "D", "S");

        Task existing = new Task("Old", "Old", TaskType.MEDIUM_PRIORITY, p);
        existing.setId(1L);

        Task updated = new Task("New", "New", TaskType.HIGH_PRIORITY, p);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.updateTask(1L, updated);

        assertEquals("New", result.getTitle());
        assertEquals("New", result.getDescription());
        assertEquals(TaskType.HIGH_PRIORITY, result.getTaskType());
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete task")
    void deleteTask() {
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }
}
