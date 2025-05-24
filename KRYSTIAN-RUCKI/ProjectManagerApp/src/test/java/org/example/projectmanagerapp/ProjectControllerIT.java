package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProjectControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-database")
            .withUsername("test")
            .withPassword("test123");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() throws Exception {
        Project project = new Project();
        project.setName("IntegrationProject");

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IntegrationProject"));

        assertTrue(projectRepository.findAll().stream().anyMatch(p -> p.getName().equals("IntegrationProject")));
    }

    @Test
    @DisplayName("Should get all projects")
    void testGetAllProjects() throws Exception {
        projectRepository.deleteAll();

        Project projectA = new Project();
        projectA.setName("ProjectA");
        projectRepository.save(projectA);

        Project projectB = new Project();
        projectB.setName("ProjectB");
        projectRepository.save(projectB);

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should get project by ID")
    void testGetProjectById() throws Exception {
        Project project = new Project();
        project.setName("ByIdProject");
        Project saved = projectRepository.save(project);

        mockMvc.perform(get("/projects/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("ByIdProject"));
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("Before Update");
        Project saved = projectRepository.save(project);

        saved.setName("After Update");

        mockMvc.perform(put("/projects/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("After Update"));
    }

    @Test
    @DisplayName("Should delete a project")
    void testDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("ToBeRemoved");
        Project saved = projectRepository.save(project);

        mockMvc.perform(delete("/projects/" + saved.getId()))
                .andExpect(status().isOk());

        Optional<Project> deleted = projectRepository.findById(saved.getId());
        assertTrue(deleted.isEmpty());
    }
}