package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
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
public class ProjectIT {
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

    @Test
    void shouldCreateProject() throws Exception {
        Project project = new Project();
        project.setName("Project A");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Project A"));
    }

    @Test
    void shouldGetProjectById() throws Exception {
        Project project = new Project();
        project.setName("Example Project");

        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long projectId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId))
                .andExpect(jsonPath("$.name").value("Example Project"));
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        // Create two projects
        for (int i = 1; i <= 2; i++) {
            Project project = new Project();
            project.setName("Project " + i);
            mockMvc.perform(post("/api/projects")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(project)))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/projects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateProject() throws Exception {
        Project original = new Project();
        original.setName("Old Name");

        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(original)))
                .andReturn().getResponse().getContentAsString();

        Long projectId = objectMapper.readTree(response).get("id").asLong();

        Project updated = new Project();
        updated.setName("New Name");

        mockMvc.perform(put("/api/projects/update/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("To Delete");

        String response = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andReturn().getResponse().getContentAsString();

        Long projectId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/projects/delete/" + projectId))
                .andExpect(status().isNoContent());
    }
}
