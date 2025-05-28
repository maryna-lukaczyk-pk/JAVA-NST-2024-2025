package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repositories.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Tag("integration")
class ProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        projectRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a new project")
    void shouldCreateProject() throws Exception {
        Project project = new Project();
        project.setName("ProjectX");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("ProjectX")));
    }

    @Test
    @DisplayName("Should return all projects")
    void shouldReturnAllProjects() throws Exception {
        Project project1 = new Project();
        project1.setName("P1");

        Project project2 = new Project();
        project2.setName("P2");

        projectRepository.save(project1);
        projectRepository.save(project2);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("P1")))
                .andExpect(jsonPath("$[1].name", is("P2")));
    }

    @Test
    @DisplayName("Should update project")
    void shouldUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("OldName");
        project = projectRepository.save(project);

        project.setName("NewName");

        mockMvc.perform(put("/api/projects/" + project.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NewName")));
    }

    @Test
    @DisplayName("Should get project by ID")
    void shouldGetProjectById() throws Exception {
        Project project = new Project();
        project.setName("FindMe");
        project = projectRepository.save(project);

        mockMvc.perform(get("/api/projects/" + project.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("FindMe")));
    }

    @Test
    @DisplayName("Should delete project")
    void shouldDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("ToDelete");
        project = projectRepository.save(project);

        mockMvc.perform(delete("/api/projects/" + project.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/projects/" + project.getId()))
                .andExpect(status().isNotFound());
    }
}


