package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("getAllTasks should return list of all tasks")
    void getAllTasks_returnsAll() {
        Task t1 = new Task(); t1.setId(1L); t1.setTitle("T1"); t1.setDescription("Desc1");
        Task t2 = new Task(); t2.setId(2L); t2.setTitle("T2"); t2.setDescription("Desc2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("createTask should save and return task")
    void createTask_savesAndReturns() {
        Task t = new Task(); t.setTitle("NewT"); t.setDescription("Desc");
        Task saved = new Task(); saved.setId(1L); saved.setTitle("NewT"); saved.setDescription("Desc");
        when(taskRepository.save(t)).thenReturn(saved);

        Task result = taskService.createTask(t);

        assertThat(result).isEqualTo(saved);
        verify(taskRepository).save(t);
    }

    @Test
    @DisplayName("getTaskById when found returns task")
    void getTaskById_found_returnsTask() {
        Task t = new Task(); t.setId(1L); t.setTitle("T"); t.setDescription("D");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));

        Task result = taskService.getTaskById(1L);

        assertThat(result).isEqualTo(t);
        verify(taskRepository).findById(1L);
    }

    @Test
    @DisplayName("updateTask when exists updates and returns")
    void updateTask_exists_updatesAndReturns() {
        Task existing = new Task(); existing.setId(1L); existing.setTitle("OldT"); existing.setDescription("OldD");
        Project p = new Project(); p.setId(1L); existing.setProject(p);
        Task details = new Task(); details.setTitle("NewT"); details.setDescription("NewD"); Project p2 = new Project(); p2.setId(2L); details.setProject(p2);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task result = taskService.updateTask(1L, details);

        assertThat(result.getTitle()).isEqualTo("NewT");
        assertThat(result.getProject().getId()).isEqualTo(2L);
        verify(taskRepository).findById(1L);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("deleteTask when exists deletes")
    void deleteTask_exists_deletes() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).existsById(1L);
        verify(taskRepository).deleteById(1L);
    }
}