package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task t1 = new Task(); t1.setTitle("T1");
        Task t2 = new Task(); t2.setTitle("T2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> res = taskService.getAllTasks();

        assertEquals(2, res.size());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should get task by ID")
    void testGetTaskById() {
        Task t = new Task(); t.setId(3L); t.setTitle("X");
        when(taskRepository.findById(3L)).thenReturn(Optional.of(t));

        Task res = taskService.getTaskById(3L);

        assertEquals("X", res.getTitle());
        verify(taskRepository).findById(3L);
    }

    @Test
    @DisplayName("Should create task")
    void testCreateTask() {
        Task t = new Task(); t.setTitle("New");
        when(taskRepository.save(t)).thenReturn(t);

        Task res = taskService.createTask(t);

        assertSame(t, res);
        verify(taskRepository).save(t);
    }

    @Test
    @DisplayName("Should update task")
    void testUpdateTask() {
        Project p = new Project(); p.setId(1L);
        Task existing = new Task(); existing.setId(4L);
        existing.setTitle("Old"); existing.setProject(p);
        Task dto      = new Task(); dto.setTitle("New"); dto.setProject(p);
        when(taskRepository.findById(4L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task res = taskService.updateTask(4L, dto);

        assertEquals("New", res.getTitle());
        verify(taskRepository).findById(4L);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete task")
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(8L);
        taskService.deleteTask(8L);
        verify(taskRepository).deleteById(8L);
    }
}
