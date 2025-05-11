package com.example.projectmanagerapp.unit.service;

import org.example.projectmanager.dto.task.TaskCreateDto;
import org.example.projectmanager.dto.task.TaskDto;
import org.example.projectmanager.dto.task.TaskEditDto;
import org.example.projectmanager.entity.task.Task;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.TaskRepository;
import org.example.projectmanager.service.ITaskService;
import org.example.projectmanager.service.TaskService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class TaskServiceTest {
    private TaskRepository repository;
    private ITaskService service;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(TaskRepository.class);
        service = new TaskService(repository);
    }

    @Test
    void FindAll_ShouldReturn_AllTasks() {
        var task = new Task();
        task.setId(1L);
        task.setTitle("Task A");

        var expected = TaskDto.MapFromEntity(task);
        Mockito.when(repository.findAll()).thenReturn(List.of(task));

        var result = service.findAll();

        Assertions.assertEquals(List.of(expected), result);
    }

    @Test
    void Create_ShouldReturn_CreatedTaskId() {
        var task = new Task();
        task.setId(1L);
        task.setTitle("Task A");
        Mockito.when(repository.save(Mockito.any(Task.class))).thenReturn(task);

        var dto = new TaskCreateDto();
        dto.title = "Task A";

        var result = service.create(dto);

        Assertions.assertEquals(task.getId(), result);
    }

    @Test
    void Delete_ShouldDelete_Task() throws EntityNotFoundException {
        var task = new Task();
        task.setId(1L);
        Mockito.when(repository.existsById(task.getId())).thenReturn(true);

        service.delete(task.getId());

        Mockito.verify(repository).deleteById(task.getId());
    }

    @Test
    void Delete_ShouldThrow_EntityNotFoundException() {
        var task = new Task();
        task.setId(1L);
        Mockito.when(repository.existsById(task.getId())).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(task.getId()));
    }

    @Test
    void Edit_ShouldUpdate_Task() throws EntityNotFoundException {
        var task = new Task();
        task.setId(1L);
        task.setTitle("Task A");
        Mockito.when(repository.findById(task.getId())).thenReturn(Optional.of(task));

        var dto = new TaskEditDto();
        dto.id = 1L;
        dto.title = "Task B";

        service.edit(dto);

        Assertions.assertEquals("Task B", task.getTitle());
    }

    @Test
    void Edit_ShouldThrow_EntityNotFoundException() {
        var dto = new TaskEditDto();
        dto.id = 1L;
        Mockito.when(repository.findById(dto.id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.edit(dto));
    }

    @Test
    void GetById_ShouldReturn_Task() throws EntityNotFoundException {
        var task = new Task();
        task.setId(1L);
        task.setTitle("Task A");
        Mockito.when(repository.findById(task.getId())).thenReturn(Optional.of(task));

        var result = service.getById(task.getId());

        Assertions.assertEquals(TaskDto.MapFromEntity(task), result);
    }
}
