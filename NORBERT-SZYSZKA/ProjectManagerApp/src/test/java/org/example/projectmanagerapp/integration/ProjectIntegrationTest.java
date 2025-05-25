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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
public class ProjectIntegrationTest {

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
    void createReadUpdateDeleteProject() throws Exception {
        // CREATE
        Map<String, Object> project = new HashMap<>();
        project.put("name", "Integration Project");
        String projectJson = objectMapper.writeValueAsString(project);

        MvcResult createResult = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Integration Project"))
                .andReturn();

        Map<String, Object> created = objectMapper.readValue(
                createResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer projectId = (Integer) created.get("id");

        // READ
        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.name").value("Integration Project"));

        // UPDATE
        project.put("name", "Updated Project");
        String updatedJson = objectMapper.writeValueAsString(project);

        mockMvc.perform(put("/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"));

        // DELETE
        mockMvc.perform(delete("/projects/" + projectId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isNotFound());
    }

    @Test
    void manageProjectUsersRelation() throws Exception {
        // create project
        Map<String, Object> project = new HashMap<>();
        project.put("name", "Relational Project");
        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult pRes = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andReturn();
        Map<String, Object> pMap = objectMapper.readValue(
                pRes.getResponse().getContentAsString(), new TypeReference<>() {}
        );
        Integer projectId = (Integer) pMap.get("id");

        // create user
        Map<String, Object> user = new HashMap<>();
        user.put("username", "testuser");
        String userJson = objectMapper.writeValueAsString(user);
        MvcResult uRes = mockMvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andReturn();
        Map<String, Object> uMap = objectMapper.readValue(
                uRes.getResponse().getContentAsString(), new TypeReference<>() {}
        );
        Integer userId = (Integer) uMap.get("id");

        // update project to include user
        project.put("users", Collections.singletonList(Map.of("id", userId)));
        String relJson = objectMapper.writeValueAsString(project);
        mockMvc.perform(put("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(relJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(userId))
                .andExpect(jsonPath("$.users[0].username").value("testuser"));
    }
}