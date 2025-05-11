package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.tasks.TaskType;
import org.example.projectmanagerapp.entity.tasks.Tasks;
import org.example.projectmanagerapp.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;

    @BeforeEach
    void setUp() throws Exception {
        taskService = Mockito.mock(TaskService.class);
        taskController = new TaskController();

        java.lang.reflect.Field taskServiceField = TaskController.class.getDeclaredField("taskService");
        taskServiceField.setAccessible(true);
        taskServiceField.set(taskController, taskService);
    }

    @Test
    void testGetAllTasks() {
        Tasks task1 = new Tasks();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setTaskType(TaskType.LOW_PRIORITY);

        Tasks task2 = new Tasks();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setTaskType(TaskType.HIGH_PRIORITY);

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        List<Tasks> tasks = taskController.getAllTasks();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void testGetTaskByIdFound() {
        Tasks task = new Tasks();
        task.setId(1L);
        task.setTitle("Found Task");
        task.setTaskType(TaskType.MEDIUM_PRIORITY);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        ResponseEntity<Tasks> response = taskController.getTaskById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Found Task", response.getBody().getTitle());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testGetTaskByIdNotFound() {
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Tasks> response = taskController.getTaskById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void testCreateTask() {
        Tasks inputTask = new Tasks();
        inputTask.setTitle("New Task");
        inputTask.setTaskType(TaskType.HIGH_PRIORITY);
        inputTask.setDescription("Description");

        Project project = new Project();
        project.setId(1L);
        inputTask.setProject(project);

        Tasks savedTask = new Tasks();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setTaskType(TaskType.HIGH_PRIORITY);
        savedTask.setDescription("Description");
        savedTask.setProject(project);

        when(taskService.createTask(any(Tasks.class))).thenReturn(savedTask);

        Tasks result = taskController.createTask(inputTask);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Task", result.getTitle());
        assertEquals(TaskType.HIGH_PRIORITY, result.getTaskType());

        verify(taskService, times(1)).createTask(inputTask);
    }

    @Test
    void testUpdateTaskFound() {
        Long id = 1L;
        Tasks updatedDetails = new Tasks();
        updatedDetails.setTitle("Updated Task");
        updatedDetails.setTaskType(TaskType.MEDIUM_PRIORITY);
        updatedDetails.setDescription("Updated description");

        Project project = new Project();
        project.setId(2L);
        updatedDetails.setProject(project);

        Tasks updatedTask = new Tasks();
        updatedTask.setId(id);
        updatedTask.setTitle("Updated Task");
        updatedTask.setTaskType(TaskType.MEDIUM_PRIORITY);
        updatedTask.setDescription("Updated description");
        updatedTask.setProject(project);

        when(taskService.updateTask(eq(id), any(Tasks.class))).thenReturn(updatedTask);

        ResponseEntity<Tasks> response = taskController.updateTask(id, updatedDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Task", response.getBody().getTitle());
        assertEquals(TaskType.MEDIUM_PRIORITY, response.getBody().getTaskType());

        verify(taskService, times(1)).updateTask(id, updatedDetails);
    }

    @Test
    void testUpdateTaskNotFound() {
        Long id = 1L;
        Tasks updatedDetails = new Tasks();
        updatedDetails.setTitle("Updated Task");

        when(taskService.updateTask(eq(id), any(Tasks.class))).thenReturn(null);

        ResponseEntity<Tasks> response = taskController.updateTask(id, updatedDetails);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(taskService, times(1)).updateTask(id, updatedDetails);
    }

    @Test
    void testDeleteTaskFound() {
        Long id = 1L;

        when(taskService.getTaskById(id)).thenReturn(Optional.of(new Tasks()));

        ResponseEntity<Void> response = taskController.deleteTask(id);

        assertEquals(204, response.getStatusCodeValue());

        verify(taskService, times(1)).getTaskById(id);
        verify(taskService, times(1)).deleteTask(id);
    }

    @Test
    void testDeleteTaskNotFound() {
        Long id = 1L;

        when(taskService.getTaskById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = taskController.deleteTask(id);

        assertEquals(404, response.getStatusCodeValue());

        verify(taskService, times(1)).getTaskById(id);
        verify(taskService, never()).deleteTask(anyLong());
    }
}