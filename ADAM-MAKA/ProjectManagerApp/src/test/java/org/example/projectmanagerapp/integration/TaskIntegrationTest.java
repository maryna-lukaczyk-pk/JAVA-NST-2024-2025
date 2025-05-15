package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private int createProjectAndGetId() throws Exception {
        String response = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"TestProject\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asInt();
    }

    @Test
    void createTask() throws Exception {
        int projectId = createProjectAndGetId();

        String taskJson = "{\"title\":\"Task1\",\"project\":{\"id\":" + projectId + "}}";
        String postResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int taskId = objectMapper.readTree(postResponse).get("id").asInt();

        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(taskId)))
                .andExpect(jsonPath("$.title", is("Task1")));
    }

    @Test
    void updateTask() throws Exception {
        int projectId = createProjectAndGetId();

        String taskJson = "{\"title\":\"Task1\",\"projectId\":" + projectId + "}";
        String postResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int taskId = objectMapper.readTree(postResponse).get("id").asInt();

        String updateJson = "{\"title\":\"TaskUpdated\",\"projectId\":" + projectId + "}";
        mockMvc.perform(put("/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("TaskUpdated")));
    }

    @Test
    void deleteTask() throws Exception {
        int projectId = createProjectAndGetId();

        String taskJson = "{\"title\":\"Task1\",\"projectId\":" + projectId + "}";
        String postResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int taskId = objectMapper.readTree(postResponse).get("id").asInt();

        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }
}