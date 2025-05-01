package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.service.Priority;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TasksRepository tasksRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        tasksRepository = mock(TasksRepository.class);
        taskService = new TaskService(tasksRepository);
    }

    @Test
    void testGetTaskByIdSuccess() {
        Tasks task = new Tasks();
        task.setId(1L);
        task.setTitle("Task Title");
        task.setDescription("Task Description");

        when(tasksRepository.findById(1L)).thenReturn(Optional.of(task));

        Tasks result = taskService.getTaskById(1L);
        assertEquals("Task Title", result.getTitle());
        assertEquals("Task Description", result.getDescription());
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(tasksRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testCreateTask() {
        Tasks task = new Tasks();
        task.setTitle("New Task");
        task.setDescription("New Task Description");

        Tasks savedTask = new Tasks();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Task Description");
        savedTask.setTask_type(Priority.LOW); // Zakładając, że domyślny priorytet to LOW

        when(tasksRepository.save(task)).thenReturn(savedTask);

        Tasks result = taskService.createTask(task);

        assertNotNull(result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals("New Task Description", result.getDescription());
        assertEquals(Priority.LOW, result.getTask_type()); // Sprawdzenie, czy priorytet został ustawiony
    }

    @Test
    void testUpdateTaskSuccess() {
        Tasks existingTask = new Tasks();
        existingTask.setId(1L);
        existingTask.setTitle("Old Title");
        existingTask.setDescription("Old Description");
        existingTask.setTask_type(Priority.LOW); // Zakładając, że początkowy priorytet to LOW

        Tasks updatedDetails = new Tasks();
        updatedDetails.setTitle("New Title");
        updatedDetails.setDescription("New Description");
        updatedDetails.setTask_type(Priority.HIGH); // Zaktualizowany priorytet

        when(tasksRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(tasksRepository.save(any(Tasks.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Tasks result = taskService.updateTask(1L, updatedDetails);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        assertEquals(Priority.HIGH, result.getTask_type()); // Sprawdzenie zaktualizowanego priorytetu
    }

    @Test
    void testUpdateTaskNotFound() {
        Tasks updatedDetails = new Tasks();
        updatedDetails.setTitle("New Title");
        updatedDetails.setDescription("New Description");
        updatedDetails.setTask_type(Priority.HIGH); // Zaktualizowany priorytet

        when(tasksRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> taskService.updateTask(1L, updatedDetails));
    }

    @Test
    void testDeleteTaskSuccess() {
        when(tasksRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(tasksRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTaskNotFound() {
        when(tasksRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void testGetPriorityLow() {
        LowPriority lowPriority = new LowPriority();
        Tasks tasks = new Tasks();
        tasks.setTask_type(lowPriority.getPriority());
        assertEquals(Priority.LOW, tasks.getTask_type());

    }

    @Test
    void testGetPriorityMedium() {
        MediumPriority mediumPriority = new MediumPriority();
        Tasks tasks = new Tasks();
        tasks.setTask_type(mediumPriority.getPriority());
        assertEquals(Priority.MEDIUM, tasks.getTask_type());

    }

    @Test
    void testGetPriorityHigh() {
        HighPriority highPriority = new HighPriority();
        Tasks tasks = new Tasks();
        tasks.setTask_type(highPriority.getPriority());
        assertEquals(Priority.HIGH, tasks.getTask_type());

    }
}
