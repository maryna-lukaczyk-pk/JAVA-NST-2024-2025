package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.interfaces.TaskRequestDTO;
import org.example.projectmanagerapp.interfaces.TaskResponseDTO;
import org.example.projectmanagerapp.model.PriorityLevelEnum;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskService = new TaskService(taskRepository, projectRepository);

    }

    @Test
    void getAllTasks_returnsListOfTaskResponseDTO() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task1");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<TaskResponseDTO> result = taskService.getAllTasks();
        assertEquals(2, result.size());
        assertEquals("Task1", result.get(0).getTitle());
        assertEquals("Task2", result.get(1).getTitle());
    }

    @Test
    void getTaskById_returnsTaskResponseDTO_whenTaskExists() {
        Task task = new Task();
        task.setId(10L);
        task.setTitle("TestTask");
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        TaskResponseDTO dto = taskService.getTaskById(10L);
        assertEquals(10L, dto.getId());
        assertEquals("TestTask", dto.getTitle());
    }

    @Test
    void getTaskById_throwsException_whenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> taskService.getTaskById(99L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void createTask_savesAndReturnsTaskResponseDTO() {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("NewTask");
        request.setDescription("desc");
        request.setTaskType(PriorityLevelEnum.HIGH);
        request.setProjectId(1L);
        Project project = new Project("Project1");
        project.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        Task savedTask = new Task();
        savedTask.setId(20L);
        savedTask.setTitle("NewTask");
        savedTask.setDescription("desc");
        savedTask.setTaskType(PriorityLevelEnum.HIGH);
        savedTask.setProject(project);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO dto = taskService.createTask(request);
        assertEquals(20L, dto.getId());
        assertEquals("NewTask", dto.getTitle());
        assertEquals("desc", dto.getDescription());
        assertEquals(PriorityLevelEnum.HIGH, dto.getTaskType());
    }

    @Test
    void createTask_throwsException_whenProjectNotFound() {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setProjectId(2L);
        when(projectRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> taskService.createTask(request));
    }

    @Test
    void updateTask_updatesAndReturnsTaskResponseDTO() {
        Task existingTask = new Task();
        existingTask.setId(30L);
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("OldDesc");
        existingTask.setTaskType(PriorityLevelEnum.LOW);
        Task updateBody = new Task();
        updateBody.setTitle("NewTitle");
        updateBody.setDescription("NewDesc");
        updateBody.setTaskType(PriorityLevelEnum.HIGH);
        when(taskRepository.findById(30L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        TaskResponseDTO dto = taskService.updateTask(30L, updateBody);
        assertEquals(30L, dto.getId());
        assertEquals("NewTitle", dto.getTitle());
        assertEquals("NewDesc", dto.getDescription());
        assertEquals(PriorityLevelEnum.HIGH, dto.getTaskType());
    }

    @Test
    void updateTask_throwsException_whenTaskNotFound() {
        Task updateBody = new Task();
        when(taskRepository.findById(40L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> taskService.updateTask(40L, updateBody));
    }

    @Test
    void deleteTask_deletesTask_whenTaskExists() {
        Task task = new Task();
        task.setId(50L);
        when(taskRepository.findById(50L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);
        taskService.deleteTask(50L);
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_throwsException_whenTaskNotFound() {
        when(taskRepository.findById(60L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> taskService.deleteTask(60L));
    }
}
