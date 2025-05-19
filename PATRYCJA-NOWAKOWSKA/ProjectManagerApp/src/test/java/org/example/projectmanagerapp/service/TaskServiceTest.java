package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private final TaskRepository taskRepository = Mockito.mock(TaskRepository.class);
    private final TaskService taskService = new TaskService(taskRepository);

    @Test
    @DisplayName("Powinien zwrócić wszystkie zadania")
    void testGetAllTasks() {
        Task t1 = new Task();
        t1.setName("Zadanie 1");

        Task t2 = new Task();
        t2.setName("Zadanie 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> result = taskService.getAllTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }
}
