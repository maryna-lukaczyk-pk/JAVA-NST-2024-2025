package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.JsonNode;
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
class ProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void assignUserToProject() throws Exception {
        // Create User
        JsonNode user = objectMapper.readTree(
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"michael\",\"email\":\"michael@example.com\"}"))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString()
        );

        // Create Project
        JsonNode project = objectMapper.readTree(
                mockMvc.perform(post("/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Alpha\"}"))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString()
        );

        int userId = user.get("id").asInt();
        int projectId = project.get("id").asInt();

        // Add user to project
        mockMvc.perform(post("/projects/{id}/users", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":" + userId + "}"))
                .andExpect(status().isOk());

        // Check if user added
        mockMvc.perform(get("/projects/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId)));
    }


    @Test
    void updateProject() throws Exception {
        JsonNode created = objectMapper.readTree(
                mockMvc.perform(post("/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Alpha\"}"))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString()
        );
        int projectId = created.get("id").asInt();

        String newName = "Beta";
        mockMvc.perform(put("/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + newName + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId)))
                .andExpect(jsonPath("$.name", is(newName)));

        mockMvc.perform(get("/projects/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newName)));
    }

    @Test
    void deleteProject() throws Exception {
        JsonNode created = objectMapper.readTree(
                mockMvc.perform(post("/projects")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Gamma\"}"))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString()
        );
        int projectId = created.get("id").asInt();

        mockMvc.perform(delete("/projects/{id}", projectId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/projects/{id}", projectId))
                .andExpect(status().isNotFound());
    }
}