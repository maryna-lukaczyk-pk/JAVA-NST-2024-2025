package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.schemas.TaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
    private TaskRepository taskRepository;
    private TaskService taskService;
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        projectRepository = Mockito.mock(ProjectRepository.class);
        taskService = new TaskService(taskRepository, projectRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task1");

        Task task2 = new Task();
        task2.setTitle("Task2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.findAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testFindTaskById() throws NotFoundException {
        Task task = new Task();
        task.setTitle("Task1");

        when(taskRepository.findById(1)).thenReturn(Optional.of(task));

        Task result = taskService.findTaskById(1);

        assertEquals("Task1", result.getTitle());
        verify(taskRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should add new task if project exists")
    void testAddTask_WhenProjectExists() {
        Project project = new Project();
        project.setId(1);

        TaskDTO dto = new TaskDTO();
        dto.setTitle("Task1");
        dto.setDescription("Opis");
        dto.setTaskType(TaskType.MEDIUM);
        dto.setProjectId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        taskService.addTask(dto);

        verify(taskRepository, times(1))
                .save(argThat(task ->
                        task.getTitle().equals("Task1") &&
                        task.getDescription().equals("Opis") &&
                        task.getTaskType().equals(TaskType.MEDIUM) &&
                        task.getProject().equals(project)
                ));
    }

    @Test
    @DisplayName("Should throw exception when adding task to non-existing project")
    void testAddTask_WhenProjectNotFound() {
        TaskDTO dto = new TaskDTO();
        dto.setProjectId(99);

        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        HttpClientErrorException ex = assertThrows(HttpClientErrorException.class, () ->
                    taskService.addTask(dto)
                );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("Project with id=99 not found"));
    }

    @Test
    @DisplayName("Should delete task if exists")
    void testDeleteTask_WhenExists() throws NotFoundException {
        when(taskRepository.existsById(1)).thenReturn(true);

        taskService.deleteTask(1);

        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existing task")
    void testDeleteTask_WhenNotExists() {
        when(taskRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(1));
    }

    @Test
    @DisplayName("Should update task attributes when task exists")
    void testUpdateTaskAttributes_WhenExists() throws NotFoundException {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Old Title");
        existingTask.setTaskType(TaskType.HIGH);

        TaskDTO dto = new TaskDTO();
        dto.setTitle("New Title");
        dto.setDescription("New Desc");
        dto.setTaskType(TaskType.MEDIUM);

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        taskService.updateTaskAttributes(dto, 1);

        assertEquals("New Title", existingTask.getTitle());
        assertEquals("New Desc", existingTask.getDescription());
        assertEquals(TaskType.MEDIUM, existingTask.getTaskType());

        verify(taskRepository).save(existingTask);
    }

    @Test
    @DisplayName("Should update nothing when all DTO fields are null")
    void testUpdateTaskAttributesWithAllNulls() throws NotFoundException {
        Task existingTask = new Task();
        existingTask.setId(1);
        existingTask.setTitle("Old title");
        existingTask.setDescription("Old desc");
        existingTask.setTaskType(TaskType.LOW);

        when(taskRepository.findById(1)).thenReturn(Optional.of(existingTask));

        TaskDTO dto = new TaskDTO();

        taskService.updateTaskAttributes(dto, 1);

        verify(taskRepository).save(argThat(savedTask ->
                savedTask.getTitle().equals("Old title") &&
                        savedTask.getDescription().equals("Old desc") &&
                        savedTask.getTaskType().equals(TaskType.LOW)
        ));
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existing task")
    void testUpdateTaskAttributes_WhenNotFound() {
        when(taskRepository.findById(1)).thenReturn(Optional.empty());

        TaskDTO dto = new TaskDTO();
        dto.setTitle("X");

        assertThrows(NotFoundException.class, () -> taskService.updateTaskAttributes(dto, 1));
    }
}
