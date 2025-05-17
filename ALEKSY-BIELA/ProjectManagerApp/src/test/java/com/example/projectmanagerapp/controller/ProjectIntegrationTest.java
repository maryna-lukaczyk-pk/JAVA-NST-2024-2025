package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProjectIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProjectIntegrationTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("getProjectById")
    void getProjectById() throws Exception {
        Project project = new Project();
        project.setName("New Project");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Project")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("getAllProjects")
    void getAllProjects() throws Exception {
        createProject("New Project");
        createProject("New Project2");
        createProject("New Project3");

        mockMvc.perform(get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(jsonPath("$[0].name", is("New Project")))
                .andExpect(jsonPath("$[1].name", is("New Project2")))
                .andExpect(jsonPath("$[2].name", is("New Project3")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("createProject")
    void createProjectTest() throws Exception {
        createProject("New Project");
    }

    @Test
    @DisplayName("updateProject")
    void updateProject() throws Exception {
        createProject("New Project");

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");

        mockMvc.perform(put("/api/projects/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Project")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("deleteProject")
    void deleteProject() throws Exception {
        createProject("Project to Delete");

        mockMvc.perform(delete("/api/projects/{id}", 1))
                .andExpect(status().is2xxSuccessful());

        mockMvc.perform(get("/api/projects/{id}", 1))
                .andExpect(status().is4xxClientError());
    }

    private void createProject(String name) throws Exception {
        Project project = new Project();
        project.setName(name);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(status().is2xxSuccessful());
    }
}
