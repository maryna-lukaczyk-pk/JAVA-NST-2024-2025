package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Project testProject;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();

        testProject = new Project();
        testProject.setName("Integration Project");
        testProject = projectRepository.save(testProject);
    }

    @Test
    @DisplayName("Should return all projects")
    void testAllProjects() throws Exception {
        mockMvc.perform(get("/api/projects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Integration Project")));
    }

    @Test
    @DisplayName("Should return project by ID")
    void testProjectById() throws Exception {
        mockMvc.perform(get("/api/projects/" + testProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Project"));
    }

    @Test
    @DisplayName("Should create new project")
    void testNewProject() throws Exception {
        Project newProject = new Project();
        newProject.setName("New Integration Project");

        mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Integration Project"));

        assertTrue(projectRepository.findAll().stream()
                .anyMatch(p -> p.getName().equals("New Integration Project")));
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() throws Exception {
        Project updated = new Project();
        updated.setName("Updated Integration Project");

        mockMvc.perform(put("/api/projects/" + testProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Integration Project"));

        Optional<Project> result = projectRepository.findById(testProject.getId());
        assertTrue(result.isPresent());
        assertEquals("Updated Integration Project", result.get().getName());
    }

    @Test
    @DisplayName("Should delete project")
    void testDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/projects/" + testProject.getId()))
                .andExpect(status().isOk());

        assertFalse(projectRepository.findById(testProject.getId()).isPresent());
    }
}
