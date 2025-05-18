package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.repository.ProjectRepository;
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
public class ProjectIntegrationTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testCreateAndFetchProject() throws Exception {
        String json = "{\"name\":\"Projekt integracyjny 1\"}";

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projekt integracyjny 1"));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.name == 'Projekt integracyjny 1')]").exists());
    }

    @Test
    public void testUpdateProjectName() throws Exception {
        // tworzy nowy projekt
        String createJson = "{\"name\":\"Projekt Startowy\"}";

        String responseBody = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode node =
                new com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
        Long projectId = node.get("id").asLong();

        // aktualizacja projektu
        String updateJson = "{\"id\":" + projectId + ",\"name\":\"Zmieniony Projekt\"}";

        mockMvc.perform(put("/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zmieniony Projekt"));

        // sprawdzenie czy zaktualizowany
        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Zmieniony Projekt"));
    }

    @Test
    public void testDeleteProject() throws Exception {

        String createJson = "{\"name\":\"Projekt do usunięcia\"}";

        String responseBody = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode node =
                new com.fasterxml.jackson.databind.ObjectMapper().readTree(responseBody);
        Long projectId = node.get("id").asLong();


        mockMvc.perform(delete("/projects/" + projectId))
                .andExpect(status().isOk());


        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }


}
