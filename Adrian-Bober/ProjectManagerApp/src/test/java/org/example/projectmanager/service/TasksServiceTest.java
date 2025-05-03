package org.example.projectmanager.service;

import org.example.projectmanager.entity.Tasks;
import org.example.projectmanager.repository.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TasksServiceTest {

    @Mock
    private TasksRepository tasksRepository;

    @InjectMocks
    private TasksService tasksService;

    private Tasks task1;
    private Tasks task2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("Task 1");

        task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("Task 2");
    }

    @Test
    void getAllTasks() {
        when(tasksRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = tasksService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(tasksRepository, times(1)).findAll();
    }

    @Test
    void createTask() {
        when(tasksRepository.save(any(Tasks.class))).thenReturn(task1);

        Tasks createdTask = tasksService.createTask(task1);

        assertEquals("Task 1", createdTask.getTitle());
        verify(tasksRepository, times(1)).save(task1);
    }
}