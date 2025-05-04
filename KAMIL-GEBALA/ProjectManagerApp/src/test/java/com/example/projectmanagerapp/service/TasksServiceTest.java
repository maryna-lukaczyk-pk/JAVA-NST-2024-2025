package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksServiceTest {

    @Mock
    private TasksRepository tasksRepository;

    private TasksService tasksService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tasksService = new TasksService(tasksRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void getAllTasks() {

        Tasks task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("TestTask1");
        task1.setDescription("Description 1");

        Tasks task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("TestTask2");
        task2.setDescription("Description 2");

        when(tasksRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> result = tasksService.getAllTasks();

        assertEquals(2, result.size());
        verify(tasksRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new task")
    void createTask() {
        Tasks task = new Tasks();
        task.setTitle("NewTask");
        task.setDescription("New task description");

        when(tasksRepository.save(any(Tasks.class))).thenReturn(task);

        Tasks result = tasksService.createTask(task);

        assertNotNull(result);
        assertEquals("NewTask", result.getTitle());
        verify(tasksRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should update an existing task")
    void updateTask_whenTaskExists() {
        long taskId = 1L;
        Tasks task = new Tasks();
        task.setId(taskId);
        task.setTitle("UpdatedTask");
        task.setDescription("Updated description");

        when(tasksRepository.existsById(taskId)).thenReturn(true);
        when(tasksRepository.save(any(Tasks.class))).thenReturn(task);

        Tasks result = tasksService.updateTask(taskId, task);

        assertNotNull(result);
        assertEquals("UpdatedTask", result.getTitle());
        verify(tasksRepository, times(1)).existsById(taskId);
        verify(tasksRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should delete a task")
    void deleteTask() {
        long taskId = 1L;

        tasksService.deleteTask(taskId);

        verify(tasksRepository, times(1)).deleteById(taskId);
    }
}