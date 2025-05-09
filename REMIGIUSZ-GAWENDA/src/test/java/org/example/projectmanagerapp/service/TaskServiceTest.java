package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.Project;
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
        Project p = new Project("Test", "Test", "ACTIVE");

        Task t1 = new Task("T1", "Desc1", TaskType.HIGH_PRIORITY, p);
        Task t2 = new Task("T2", "Desc2", TaskType.MEDIUM_PRIORITY, p);

        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void getTaskById() {
        Project p = new Project("Test", "Test", "ACTIVE");

        Task t = new Task("T1", "Desc", TaskType.LOW_PRIORITY, p);
        t.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        Task result = taskService.getTaskById(1L);

        assertEquals("T1", result.getTitle());
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create task")
    void createTask() {
        Project p = new Project("P", "D", "S");

        Task t = new Task("TaskX", "Fix it", TaskType.HIGH_PRIORITY, p);

        when(taskRepository.save(t)).thenReturn(t);

        Task result = taskService.createTask(t);

        assertEquals("TaskX", result.getTitle());
        verify(taskRepository).save(t);
    }
}
