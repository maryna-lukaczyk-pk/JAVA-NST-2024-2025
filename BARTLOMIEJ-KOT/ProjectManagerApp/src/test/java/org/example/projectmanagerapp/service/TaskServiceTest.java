package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.*;

import java.util.*;

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
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task()));
        assertEquals(2, taskService.getAllTasks().size());
    }

    @Test
    void getTaskById() {
        Task t = new Task(); t.setName("task");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        assertEquals("task", taskService.getTaskById(1L).getName());
    }

    @Test
    void createTask() {
        Task t = new Task(); t.setName("name");
        when(taskRepository.save(t)).thenReturn(t);
        assertEquals("name", taskService.createTask(t).getName());
    }

    @Test
    void updateTask() {
        Task existing = new Task(); existing.setName("old");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        Task input = new Task(); input.setName("new");
        when(taskRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals("new", taskService.updateTask(1L, input).getName());
    }

    @Test
    void deleteTask() {
        taskService.deleteTask(5L);
        verify(taskRepository).deleteById(5L);
    }
}
