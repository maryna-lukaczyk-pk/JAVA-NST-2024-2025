package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest(classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
@Transactional
public class TaskIT {
    @Container
    static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("test_db")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.format_sql", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private Task buildTask(String name, String description) {
        Task task = new Task();
        task.setTitle(name);
        task.setDescription(description);
        return task;
    }

    @Test
    void shouldCreateTask() throws Exception {
        Task task = buildTask("Test Task", "Simple task");

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Simple task"));
    }

    @Test
    void shouldGetTaskById() throws Exception {
        Task task = buildTask("Important Task", "Desc");

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId))
                .andExpect(jsonPath("$.title").value("Important Task"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        for (int i = 1; i <= 2; i++) {
            Task task = buildTask("Task " + i, "Description " + i);
            mockMvc.perform(post("/api/tasks")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(task)))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        Task task = buildTask("Old Task", "Old Desc");

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        Task updated = buildTask("Updated Task", "Updated Desc");

        mockMvc.perform(put("/api/tasks/update/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Desc"));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        Task task = buildTask("To Delete", "Desc");

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andReturn().getResponse().getContentAsString();

        Long taskId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/tasks/" + taskId))
                .andExpect(status().isNoContent());
    }
}
