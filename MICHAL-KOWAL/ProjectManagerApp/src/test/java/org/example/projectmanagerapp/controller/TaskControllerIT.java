package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.entity.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    private Task testTask1;
    private Task testTask2;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        testTask1 = new Task();
        testTask1.setTitle("Test Task 1");
        testTask1.setDescription("Description for Test Task 1");
        testTask1.setTask_type(TaskType.BUGFIX);

        testTask2 = new Task();
        testTask2.setTitle("Test Task 2");
        testTask2.setDescription("Description for Test Task 2");
        testTask2.setTask_type(TaskType.FEATURE);

        taskRepository.saveAll(List.of(testTask1, testTask2));
    }

    @Test
    void getAllTasks_ReturnsListOfTasks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(testTask1.getTitle())))
                .andExpect(jsonPath("$[1].title", is(testTask2.getTitle())));
    }

    @Test
    void getTaskById_ReturnsTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", testTask1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(testTask1.getTitle())))
                .andExpect(jsonPath("$.description", is(testTask1.getDescription())))
                .andExpect(jsonPath("$.task_type", is(testTask1.getTask_type().toString())));
    }

    @Test
    void getTaskById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTask_ReturnsCreatedTask() throws Exception {
        Task newTask = new Task();
        newTask.setTitle("New Task");
        newTask.setDescription("New Task Description");
        newTask.setTask_type(TaskType.RELEASE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(newTask.getTitle())))
                .andExpect(jsonPath("$.description", is(newTask.getDescription())))
                .andExpect(jsonPath("$.task_type", is(newTask.getTask_type().toString())));
    }

    @Test
    void updateTask_ReturnsUpdatedTask() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task Title");
        updatedTask.setDescription("Updated Task Description");
        updatedTask.setTask_type(TaskType.BUGFIX);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/update/{id}", testTask1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(updatedTask.getTitle())))
                .andExpect(jsonPath("$.description", is(updatedTask.getDescription())))
                .andExpect(jsonPath("$.task_type", is(updatedTask.getTask_type().toString())));
    }

    @Test
    void updateTask_NotFound() throws Exception {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task Title");
        updatedTask.setDescription("Updated Task Description");
        updatedTask.setTask_type(TaskType.FEATURE);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/update/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_ReturnsOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/delete/{id}", testTask1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{id}", testTask1.getId()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/delete/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}