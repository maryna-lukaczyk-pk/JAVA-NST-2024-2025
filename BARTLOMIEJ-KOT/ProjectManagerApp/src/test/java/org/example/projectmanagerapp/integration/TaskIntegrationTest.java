package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    //lab4
    @Test
    public void testCreateAndFetchTask() throws Exception {
        // tworzenie projektu
        String projectJson = "{\"name\":\"Projekt taska\"}";

        String responseBody = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode responseNode =
                new com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
        Long projectId = responseNode.get("id").asLong();

        // tworzenie taska
        String taskJson = String.format(
                "{" +
                        "\"name\":\"Task1\"," +
                        "\"description\":\"Opis\"," +
                        "\"priority\":\"MEDIUM\"," +
                        "\"project\":{\"id\":%d}" +
                        "}",
                projectId
        );

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Task1"));

        // sprawdzenie czy task istnieje
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'Task1')]").exists());
    }

}

