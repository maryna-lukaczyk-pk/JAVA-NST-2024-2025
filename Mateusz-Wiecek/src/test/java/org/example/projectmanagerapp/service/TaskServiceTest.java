package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @InjectMocks private TaskService taskService;

    private Project dummyProject;
    private Task task1, task2;

    @BeforeEach
    void setUp() {
        dummyProject = new Project(1L, "Proj");
        task1 = new Task(1L, "T1", "desc1", "TYPE1", dummyProject);
        task2 = new Task(2L, "T2", "desc2", "TYPE2", dummyProject);
    }

    @Test @DisplayName("getAllTasks returns all tasks")
    void getAllTasks() {
        when(taskRepository.findAll())
                .thenReturn(Arrays.asList(task1, task2));

        List<Task> all = taskService.getAllTasks();
        assertEquals(2, all.size());
    }

    @Test @DisplayName("getTaskById returns task when exists")
    void getTaskByIdExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        Optional<Task> t = taskService.getTaskById(1L);
        assertTrue(t.isPresent());
        assertEquals("T1", t.get().getTitle());
    }

    @Test @DisplayName("getTaskById returns empty when not exists")
    void getTaskByIdNotExists() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(taskService.getTaskById(99L).isPresent());
    }

    @Test @DisplayName("createTask saves the task")
    void createTask() {
        when(taskRepository.save(task1)).thenReturn(task1);
        Task t = taskService.createTask(task1);
        assertEquals("T1", t.getTitle());
    }

    @Test @DisplayName("updateTask updates when exists")
    void updateTaskExists() {
        Task updates = new Task(null, "T1X", "descX", "TYPEX", dummyProject);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(i -> i.getArgument(0));

        Optional<Task> up = taskService.updateTask(1L, updates);
        assertTrue(up.isPresent());
        assertEquals("T1X", up.get().getTitle());
        assertEquals("descX", up.get().getDescription());
    }

    @Test @DisplayName("updateTask returns empty when not exists")
    void updateTaskNotExists() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(taskService.updateTask(99L, task1).isPresent());
    }

    @Test @DisplayName("deleteTask returns true when it existed")
    void deleteTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        assertTrue(taskService.deleteTask(1L));
        verify(taskRepository).deleteById(1L);
    }

    @Test @DisplayName("deleteTask returns false when it did not exist")
    void deleteTaskNotExists() {
        when(taskRepository.existsById(99L)).thenReturn(false);
        assertFalse(taskService.deleteTask(99L));
        verify(taskRepository, never()).deleteById(anyLong());
    }
}
