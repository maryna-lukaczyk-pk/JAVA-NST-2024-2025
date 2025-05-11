package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.TaskDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Testy związane z serwisem zadania
class TaskServiceTest {

    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskService = new TaskService(taskRepository, projectRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Project p = new Project("P"); p.setId(1L);
        TaskType taskType = TaskType.BUG;
        Task t = new Task("T", "D", taskType, p); t.setId(2L);
        when(taskRepository.findAll()).thenReturn(Arrays.asList(t));

        var dtos = taskService.getAllTasks();

        assertEquals(1, dtos.size());
        assertEquals("T", dtos.get(0).title());
        verify(taskRepository).findAll();
    }

    @Test
    @DisplayName("Should create task when project exists")
    void testCreateTask_Success() {
        Project p = new Project("P"); p.setId(10L);
        TaskType taskType = TaskType.FEATURE;
        Task input = new Task("A", "B", taskType, p);
        input.setId(null);

        when(projectRepository.findById(10L)).thenReturn(Optional.of(p));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> {
                    Task saved = inv.getArgument(0);
                    saved.setId(99L);
                    return saved;
                });

        TaskDTO dto = taskService.createTask(input);

        assertEquals(10L, dto.project_id());
        assertEquals(99L, dto.id());
    }

    @Test
    @DisplayName("createTask throws 404 when project missing")
    void testCreateTask_ProjectNotFound() {
        Project p = new Project("P"); p.setId(5L);
        Task input = new Task("X", "Y", null, p);
        when(projectRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> taskService.createTask(input));
    }

    @Test
    @DisplayName("Should delete task or throw if missing")
    void testDeleteTask() {
        when(taskRepository.existsById(20L)).thenReturn(true);
        assertDoesNotThrow(() -> taskService.deleteTask(20L));

        when(taskRepository.existsById(21L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> taskService.deleteTask(21L));
    }

    @Test
    @DisplayName("updateTask throws 404 when task not found")
    void testUpdateTask_NotFound() {
        when(taskRepository.findById(123L)).thenReturn(Optional.empty());

        Task dummy = new Task("x","y", TaskType.FEATURE, new Project("P"));
        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(123L, dummy));
    }

    @Test
    @DisplayName("updateTask updates all fields and project")
    void testUpdateTask_FullUpdateWithProjectChange() {
        Project oldProj = new Project("Old");
        oldProj.setId(10L);
        Task existing = new Task("T", "D", TaskType.BUG, oldProj);
        existing.setPriorityLevel(PriorityLevel.MEDIUM);
        existing.setId(8L);
        when(taskRepository.findById(8L)).thenReturn(Optional.of(existing));

        Project newProj = new Project("New");
        newProj.setId(20L);
        when(projectRepository.findById(20L)).thenReturn(Optional.of(newProj));
        when(taskRepository.save(existing)).thenReturn(existing);

        Task update = new Task("T2", "D2", TaskType.FEATURE, newProj);
        update.setPriorityLevel(PriorityLevel.HIGH);

        TaskDTO dto = taskService.updateTask(8L, update);

        assertEquals("T2", dto.title());
        assertEquals("D2", dto.description());
        assertEquals(TaskType.FEATURE.name(), dto.task_type());
        assertEquals(PriorityLevel.HIGH.getPriority(), dto.priority());
        assertEquals(20L, dto.project_id());

        verify(projectRepository).findById(20L);
        verify(taskRepository).save(existing);
    }

    @Test
    @DisplayName("updateTask throws 404 when new project not found")
    void testUpdateTask_ProjectNotFoundOnChange() {
        Project oldProj = new Project("Old"); oldProj.setId(1L);
        Task existing = new Task("T","D", TaskType.BUG, oldProj);
        existing.setId(42L);
        when(taskRepository.findById(42L)).thenReturn(Optional.of(existing));

        Project fake = new Project("X"); fake.setId(99L);
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        Task update = new Task(null,null,null, fake);
        update.setPriorityLevel(null);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> taskService.updateTask(42L, update));
        assertTrue(ex.getMessage().contains("Project not found with ID: 99"));
    }

    @Test
    @DisplayName("getAllTasks propagates the set priority via setPriority")
    void testGetAllTasks_PriorityMapping() {
        Project p = new Project("P"); p.setId(1L);
        Task t = new Task("T","D", TaskType.BUG, p);
        t.setId(2L);

        for (String val : new String[]{"low", "MeDIum", "HIGH", "something"}) {
            t.setPriority(val);

            when(taskRepository.findAll()).thenReturn(List.of(t));
            var dtos = taskService.getAllTasks();

            String expected = switch(val.toUpperCase()) {
                case "LOW" -> PriorityLevel.LOW.getPriority();
                case "MEDIUM" -> PriorityLevel.MEDIUM.getPriority();
                case "HIGH" -> PriorityLevel.HIGH.getPriority();
                default -> PriorityLevel.UNDEFINED.getPriority();
            };

            assertEquals(1, dtos.size());
            assertEquals(expected, dtos.get(0).priority());
        }

        verify(taskRepository, times(4)).findAll();
    }
}