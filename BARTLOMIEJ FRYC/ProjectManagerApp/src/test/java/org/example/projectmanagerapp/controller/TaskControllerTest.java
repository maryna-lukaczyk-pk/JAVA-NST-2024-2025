package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.task_type;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    @DisplayName("Should return all tasks")
    void testGetAllTasks() throws Exception {
        // Arrange
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setTaskType(task_type.OK);

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setTaskType(task_type.NOK);

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[0].description").value("Description 1"))
                .andExpect(jsonPath("$[0].taskType").value("OK"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"))
                .andExpect(jsonPath("$[1].description").value("Description 2"))
                .andExpect(jsonPath("$[1].taskType").value("NOK"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    @DisplayName("Should return task by ID when task exists")
    void testGetTaskByIdWhenTaskExists() throws Exception {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setTaskType(task_type.OK);

        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.taskType").value("OK"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @DisplayName("Should return 404 when task does not exist")
    void testGetTaskByIdWhenTaskDoesNotExist() throws Exception {
        // Arrange
        when(taskService.getTaskById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    @DisplayName("Should create a new task")
    void testCreateTask() throws Exception {
        // Arrange
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Description");
        task.setTaskType(task_type.OK);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setTaskType(task_type.OK);

        when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"New Task\",\"description\":\"New Description\",\"taskType\":\"OK\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.taskType").value("OK"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    @DisplayName("Should update task when task exists")
    void testUpdateTaskWhenTaskExists() throws Exception {
        // Arrange
        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setTaskType(task_type.NOK);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"taskType\":\"NOK\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.taskType").value("NOK"));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent task")
    void testUpdateTaskWhenTaskDoesNotExist() throws Exception {
        // Arrange
        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"taskType\":\"NOK\"}"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("Should delete task when task exists")
    void testDeleteTaskWhenTaskExists() throws Exception {
        // Arrange
        when(taskService.deleteTask(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent task")
    void testDeleteTaskWhenTaskDoesNotExist() throws Exception {
        // Arrange
        when(taskService.deleteTask(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(1L);
    }
}