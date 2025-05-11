package org.example.projectmanagerapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTests {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;
    private Task testTask;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();

        testTask = new Task();
        testTask.setId(1L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
    }

    @Test
    @DisplayName("It should get task by ID")
    void getTask() throws Exception {
        when(taskService.findTaskById(1L)).thenReturn(Optional.of(testTask));

        mockMvc.perform(get("/api/v1/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"));

        verify(taskService, times(1)).findTaskById(1L);
    }

    @Test
    @DisplayName("It should return 404 when task not found")
    void getTaskNotFound() throws Exception {
        when(taskService.findTaskById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/task/999"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).findTaskById(999L);
    }

    @Test
    @DisplayName("It should get all tasks")
    void getAllTasks() throws Exception {
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");

        List<Task> tasks = Arrays.asList(testTask, task2);
        when(taskService.findAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/task"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).findAllTasks();
    }

    @Test
    @DisplayName("It should create a new task")
    void createTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Description");

        Project project = new Project();
        project.setId(1L);
        newTask.setProject(project);

        Task savedTask = new Task();
        savedTask.setId(3L);
        savedTask.setTitle("New Task");
        savedTask.setDescription("New Description");
        savedTask.setProject(project);

        when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    @DisplayName("It should update an existing task")
    void updateTask() throws Exception {
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Task");
        taskDetails.setDescription("Updated Description");

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/api/v1/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    @DisplayName("It should handle ResponseStatusException when updating non-existent task")
    void updateTaskNotFound() throws Exception {
        Task taskDetails = new Task();
        taskDetails.setTitle("Updated Task");

        when(taskService.updateTask(eq(999L), any(Task.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Zadanie o ID 999 nie zostało znalezione"));

        mockMvc.perform(put("/api/v1/task/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDetails)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).updateTask(eq(999L), any(Task.class));
    }

    @Test
    @DisplayName("It should delete a task")
    void deleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/v1/task/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    @DisplayName("It should handle ResponseStatusException when deleting non-existent task")
    void deleteTaskNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Zadanie o ID 999 nie zostało znalezione"))
                .when(taskService).deleteTask(999L);

        mockMvc.perform(delete("/api/v1/task/999"))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).deleteTask(999L);
    }

    @Test
    @DisplayName("It should create task with project reference")
    void createTaskWithProject() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("Task with Project");
        newTask.setDescription("Description");

        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        newTask.setProject(project);

        Task savedTask = new Task();
        savedTask.setId(10L);
        savedTask.setTitle("Task with Project");
        savedTask.setDescription("Description");
        savedTask.setProject(project);

        when(taskService.createTask(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("Task with Project"))
                .andExpect(jsonPath("$.project.id").value(1));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    @DisplayName("It should handle ResponseStatusException when creating task with non-existent project")
    void createTaskWithNonExistentProject() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("Task with Invalid Project");

        Project project = new Project();
        project.setId(999L);
        newTask.setProject(project);

        when(taskService.createTask(any(Task.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Projekt o ID 999 nie został znaleziony"));

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isNotFound());

        verify(taskService, times(1)).createTask(any(Task.class));
    }
}