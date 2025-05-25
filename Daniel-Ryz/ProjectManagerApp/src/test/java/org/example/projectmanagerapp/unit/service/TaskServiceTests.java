package org.example.projectmanagerapp.unit.service;

import org.example.projectmanagerapp.dto.CreateTaskRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.example.projectmanagerapp.service.TaskService;
import org.example.projectmanagerapp.service.priority.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTests {

    private TaskRepository taskRepository;
    private PriorityAssigner priorityAssigner;
    private ProjectService projectService;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = Mockito.mock(TaskRepository.class);
        priorityAssigner = Mockito.mock(PriorityAssigner.class);
        projectService = Mockito.mock(ProjectService.class);
        taskService = new TaskService(taskRepository, priorityAssigner, projectService);
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() {
        Tasks task1 = new Tasks();
        task1.setTitle("Task 1");

        Tasks task2 = new Tasks();
        task2.setTitle("Task 2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return task by ID")
    void testGetTaskById() {
        Tasks task = new Tasks();
        task.setTitle("Example Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Tasks result = taskService.getTasksById(1L);

        assertEquals("Example Task", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw when task not found")
    void testGetTaskByIdThrows() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.getTasksById(1L));
    }

    @Test
    @DisplayName("Should create a new task with dynamic priority")
    void testCreateTask() {
        CreateTaskRequest request = new CreateTaskRequest("Title", "Desc", TaskType.HIGH_PRIORITY);

        PriorityLevel mockPriority = new HighPriority();
        when(priorityAssigner.assignPriority("Title")).thenReturn(mockPriority);

        Tasks saved = new Tasks();
        saved.setTitle("Title");
        saved.setDynamicPriority("HIGH");

        when(taskRepository.save(any(Tasks.class))).thenReturn(saved);

        Tasks result = taskService.createTask(request);

        assertEquals("Title", result.getTitle());
        assertEquals("HIGH", result.getDynamicPriority());
        verify(taskRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Should create a task with Medium priority")
    void testCreateMediumPriorityTask() {
        CreateTaskRequest request = new CreateTaskRequest("This is a long title", "Desc", TaskType.MEDIUM_PRIORITY);

        PriorityLevel mockPriority = new MediumPriority();
        when(priorityAssigner.assignPriority("This is a long title")).thenReturn(mockPriority);

        Tasks saved = new Tasks();
        saved.setTitle("This is a long title");
        saved.setDynamicPriority("MEDIUM");

        when(taskRepository.save(any(Tasks.class))).thenReturn(saved);

        Tasks result = taskService.createTask(request);

        assertEquals("This is a long title", result.getTitle());
        assertEquals("MEDIUM", result.getDynamicPriority());
        verify(taskRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Should create a task with Low priority")
    void testCreateLowPriorityTask() {
        CreateTaskRequest request = new CreateTaskRequest("Short Title", "Desc", TaskType.LOW_PRIORITY);

        PriorityLevel mockPriority = new LowPriority();
        when(priorityAssigner.assignPriority("Short Title")).thenReturn(mockPriority);

        Tasks saved = new Tasks();
        saved.setTitle("Short Title");
        saved.setDynamicPriority("LOW");

        when(taskRepository.save(any(Tasks.class))).thenReturn(saved);

        Tasks result = taskService.createTask(request);

        assertEquals("Short Title", result.getTitle());
        assertEquals("LOW", result.getDynamicPriority());
        verify(taskRepository, times(1)).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Should update an existing task")
    void testUpdateTask() {
        Tasks existing = new Tasks();
        existing.setId(1L);
        existing.setTitle("Old");

        CreateTaskRequest request = new CreateTaskRequest("New Title", "New Desc", TaskType.MEDIUM_PRIORITY);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(priorityAssigner.assignPriority("New Title")).thenReturn(new MediumPriority());
        when(taskRepository.save(existing)).thenReturn(existing);

        Tasks result = taskService.updateTask(1L, request);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Desc", result.getDescription());
        assertEquals("MEDIUM", result.getDynamicPriority());
        verify(taskRepository, times(1)).save(existing);
    }
    @Test
    @DisplayName("Should throw exception when updating a task that does not exist")
    void testUpdateTaskThrowsExceptionWhenTaskNotFound() {

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        CreateTaskRequest request = new CreateTaskRequest("New Title", "New Description", TaskType.LOW_PRIORITY);

        assertThrows(RuntimeException.class, () -> taskService.updateTask(1L, request), "Task not found");

        verify(taskRepository, times(1)).findById(1L);

        verify(taskRepository, times(0)).save(any(Tasks.class));
    }

    @Test
    @DisplayName("Should delete task by ID")
    void testDeleteTask() {
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should assign task to a project")
    void testAssignTaskToProject() {
        Tasks task = new Tasks();
        Project project = new Project();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(projectService.getProjectById(10L)).thenReturn(project);

        taskService.assignTaskToProject(1L, 10L);

        assertEquals(project, task.getProject());
        verify(taskRepository, times(1)).save(task);
    }
}
