package org.example.projectmanagerapp.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testCreateProject() throws Exception {
        String json = """
                {
                    "name": "New Project"
                }
                """;

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void testGetProjectById() throws Exception {
        Project saved = projectRepository.save(new Project(null, "Test Project", new HashSet<>(), new ArrayList<>()));

        mockMvc.perform(get("/api/projects/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void testGetProjectById_NotFound() throws Exception {
        mockMvc.perform(get("/api/projects/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllProjects() throws Exception {
        projectRepository.save(new Project(null, "P1", new HashSet<>(), new ArrayList<>()));
        projectRepository.save(new Project(null, "P2", new HashSet<>(), new ArrayList<>()));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetAllProjects_NotFound() throws Exception {
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateProject() throws Exception {
        Project saved = projectRepository.save(new Project(null, "Old Name", new HashSet<>(), new ArrayList<>()));

        String json = """
                {
                    "name": "Updated Project"
                }
                """;

        mockMvc.perform(put("/api/projects/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void testUpdateProject_NotFound() throws Exception {
        String json = """
                {
                    "name": "Updated"
                }
                """;

        mockMvc.perform(put("/api/projects/9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProject() throws Exception {
        Project saved = projectRepository.save(new Project(null, "To Delete", new HashSet<>(), new ArrayList<>()));

        mockMvc.perform(delete("/api/projects/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Project deleted successfully."));

        mockMvc.perform(get("/api/projects/" + saved.getId()))
                .andExpect(status().isNotFound());
    }
}
