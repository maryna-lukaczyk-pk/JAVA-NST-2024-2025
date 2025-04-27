package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.TaskType;
import com.example.projectmanagerapp.priority.HighPriority;
import com.example.projectmanagerapp.priority.LowPriority;
import com.example.projectmanagerapp.priority.PriorityLevel;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository);
    }

    @Test
    @DisplayName("Should return all tasks")
    void getAllTasks() {
        Task task1 = new Task();
        task1.setTitle("TestTask1");

        Task task2 = new Task();
        task2.setTitle("TestTask2");

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new task")
    void createTask() {
        Task task = new Task();
        task.setTitle("NewTask");
        task.setDescription("Task Description");
        task.setTaskTypeEnum(TaskType.BUG);
        task.setPriority(new HighPriority());

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertNotNull(createdTask);
        assertEquals("NewTask", createdTask.getTitle());
        assertEquals("Task Description", createdTask.getDescription());
        assertEquals(TaskType.BUG, createdTask.getTaskTypeEnum());
        assertEquals("HIGH", createdTask.getPriority().getPriority());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    @DisplayName("Should return task by ID")
    void getTaskById() {
        Task task = new Task();
        task.setTitle("TestTask");
        task.setDescription("Test Description");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = taskService.getTaskById(1L);

        assertNotNull(foundTask);
        assertEquals("TestTask", foundTask.getTitle());
        assertEquals("Test Description", foundTask.getDescription());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when task not found by ID")
    void getTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.getTaskById(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update task")
    void updateTask() {
        Project project = new Project();
        project.setName("TestProject");

        Task existingTask = new Task();
        existingTask.setTitle("OldTitle");
        existingTask.setDescription("Old Description");
        existingTask.setTaskTypeEnum(TaskType.BUG);
        existingTask.setPriority(new HighPriority());

        Task updatedDetails = new Task();
        updatedDetails.setTitle("NewTitle");
        updatedDetails.setDescription("New Description");
        updatedDetails.setTaskTypeEnum(TaskType.FEATURE);
        updatedDetails.setPriority(new LowPriority());
        updatedDetails.setProject(project);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task updatedTask = taskService.updateTask(1L, updatedDetails);

        assertNotNull(updatedTask);
        assertEquals("NewTitle", updatedTask.getTitle());
        assertEquals("New Description", updatedTask.getDescription());
        assertEquals(TaskType.FEATURE, updatedTask.getTaskTypeEnum());
        assertEquals("LOW", updatedTask.getPriority().getPriority());
        assertEquals("TestProject", updatedTask.getProject().getName());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    @DisplayName("Should delete task")
    void deleteTask() {
        Task task = new Task();
        task.setTitle("TaskToDelete");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Should throw exception when task not found during delete")
    void deleteTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).delete(any(Task.class));
    }

    @Test
    @DisplayName("Should throw exception when task not found during update")
    void updateTask_NotFound() {
        Task updatedDetails = new Task();
        updatedDetails.setTitle("UpdatedTask");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, updatedDetails);
        });

        assertEquals("Task not found with id: 1", exception.getMessage());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }
}
