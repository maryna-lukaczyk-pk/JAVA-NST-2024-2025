package com.example.demo.service;

import com.example.demo.entity.Tasks;
import com.example.demo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return all tasks")
    void getAllTasks() {
        //Tworzymy testowe taski
        Tasks task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("TestTask1");
        task1.setDescription("Description 1");
        task1.setPriority("HIGH");

        Tasks task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("TestTask2");
        task2.setDescription("Description 2");
        task2.setPriority("MEDIUM");

        //konfiguracja mocka
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        //odpalamy metode
        List<Tasks> result = taskService.getAllTasks();

        //sprawdzamy wyniki
        assertEquals(2, result.size());
        assertEquals("TestTask1", result.get(0).getTitle());
        assertEquals("TestTask2", result.get(1).getTitle());
        assertEquals("HIGH", result.get(0).getPriority());
        assertEquals("MEDIUM", result.get(1).getPriority());

    }

    @Test
    @DisplayName("Should create a new task")
    void createTask() {
        Tasks task = new Tasks();
        task.setTitle("NewTask");
        task.setDescription("New Description");
        task.setPriority("LOW");

        when(taskRepository.save(any(Tasks.class))).thenReturn(task);

        Tasks result = taskService.createTask(task);

        assertNotNull(result);
        assertEquals("NewTask", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals("LOW", result.getPriority());

    }

    @Test
    @DisplayName("Should update an existing task")
    void updateTask() {
        Long taskId = 1L;
        Tasks updatedTask = new Tasks();
        updatedTask.setTitle("UpdatedTask");
        updatedTask.setDescription("Updated Description");
        updatedTask.setPriority("HIGH");

        when(taskRepository.existsById(taskId)).thenReturn(true);
        when(taskRepository.save(any(Tasks.class))).thenReturn(updatedTask);

        Tasks result = taskService.updateTask(taskId, updatedTask);

        assertNotNull(result);
        assertEquals("UpdatedTask", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals("HIGH", result.getPriority());
        assertEquals(taskId, updatedTask.getId());

    }

    @Test
    @DisplayName("Should delete a task")
    void deleteTask() {
        Long taskId = 1L;

        taskService.deleteTask(taskId);

    }
}