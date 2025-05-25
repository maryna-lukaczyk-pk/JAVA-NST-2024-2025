package org.example.projectmanagerapp.integration;

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
public class TaskIntegrationTestIT {

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

    @Test
    public void testUpdateTask() throws Exception {
        // nowy projekt
        String projectJson = "{\"name\":\"Projekt Testowy\"}";
        String projectResponse = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Long projectId = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(projectResponse).get("id").asLong();

        // nowy task
        String createTaskJson = String.format(
                "{\"name\":\"Zadanie X\", \"priorityLevel\": {\"type\": \"MEDIUM\"}, \"project\":{\"id\":%d}}",
                projectId
        );

        String taskResponse = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createTaskJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        Long taskId = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(taskResponse).get("id").asLong();

        // aktualizacja taska
        String updateTaskJson = String.format(
                "{\"id\":%d, \"name\":\"Zadanie Zaktualizowane\", \"priorityLevel\": {\"type\": \"MEDIUM\"}, \"project\":{\"id\":%d}}",
                taskId, projectId
        );

        mockMvc.perform(put("/tasks/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTaskJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zadanie Zaktualizowane"))
                .andExpect(jsonPath("$.priorityLevel.type").value("MEDIUM"));
    }


}

