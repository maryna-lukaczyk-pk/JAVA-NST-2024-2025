package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class TaskIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine")
            .withDatabaseName("testdb")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createReadUpdateDeleteTask() throws Exception {
        // FIRST create a project to attach
        Map<String, Object> project = new HashMap<>();
        project.put("name", "TaskProject");
        String projJson = objectMapper.writeValueAsString(project);
        MvcResult pRes = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projJson))
                .andReturn();
        Map<String, Object> pMap = objectMapper.readValue(
                pRes.getResponse().getContentAsString(), new TypeReference<>() {}
        );
        Integer projectId = (Integer) pMap.get("id");

        // CREATE TASK
        Map<String, Object> task = new HashMap<>();
        task.put("title", "Integration Task");
        task.put("description", "Task desc");
        task.put("project", Map.of("id", projectId));
        String taskJson = objectMapper.writeValueAsString(task);

        MvcResult createRes = mockMvc.perform(post("/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andExpect(jsonPath("$.project.id").value(projectId))
                .andReturn();

        Map<String, Object> created = objectMapper.readValue(
                createRes.getResponse().getContentAsString(), new TypeReference<>() {}
        );
        Integer taskId = (Integer) created.get("id");

        // READ
        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Task"));

        // UPDATE
        task.put("title", "Updated Task");
        String updJson = objectMapper.writeValueAsString(task);
        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));

        // DELETE
        mockMvc.perform(delete("/tasks/" + taskId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/" + taskId))
                .andExpect(status().isNotFound());
    }
}