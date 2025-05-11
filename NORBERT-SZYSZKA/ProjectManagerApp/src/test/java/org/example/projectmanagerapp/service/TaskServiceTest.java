package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @InjectMocks private TaskService taskService;
    @Captor private ArgumentCaptor<Task> captor;

    private Task t1, t2;

    @BeforeEach
    void setUp() {
        t1 = new Task(); t1.setId(1L); t1.setTitle("T1"); t1.setPriorityLevel(() -> "High Priority");
        t2 = new Task(); t2.setId(2L); t2.setTitle("T2"); t2.setPriorityLevel(() -> "Low Priority");
    }

    @Test
    void findAll_returnsAll() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Task> result = taskService.findAll();
        assertThat(result).containsExactly(t1, t2);
        verify(taskRepository).findAll();
    }

    @Test
    void findById_existing_returnsTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t1));

        Optional<Task> result = taskService.findById(1L);
        assertThat(result).contains(t1);
        verify(taskRepository).findById(1L);
    }

    @Test
    void findById_nonExisting_returnsEmpty() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Task> result = taskService.findById(99L);
        assertThat(result).isEmpty();
        verify(taskRepository).findById(99L);
    }

    @Test
    void create_savesAndReturns() {
        Task toSave = new Task(); toSave.setTitle("New"); toSave.setPriorityLevel(() -> "Medium Priority");
        when(taskRepository.save(any(Task.class))).thenReturn(t1);

        Task result = taskService.create(toSave);
        assertThat(result).isEqualTo(t1);
        verify(taskRepository).save(toSave);
    }

    @Test
    void update_setsIdAndSaves() {
        Task upd = new Task(); upd.setTitle("Upd"); upd.setPriorityLevel(() -> "Medium Priority");
        when(taskRepository.save(any(Task.class))).thenReturn(upd);

        Task result = taskService.update(3L, upd);
        assertThat(result).isEqualTo(upd);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(3L);
    }

    @Test
    void delete_invokesRepository() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.delete(1L);
        verify(taskRepository).deleteById(1L);
    }
}
