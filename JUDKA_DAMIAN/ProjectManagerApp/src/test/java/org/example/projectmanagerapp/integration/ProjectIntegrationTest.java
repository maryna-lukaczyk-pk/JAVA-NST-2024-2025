package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.ProjectManagerAppApplication;
import org.example.projectmanagerapp.schemas.ProjectDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateProject() throws Exception {
        ProjectDTO dto = new ProjectDTO();
        dto.setName("Project1");

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllProjects() throws Exception {
        // Assuming the project has been added
        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void shouldGetProjectById() throws Exception {
        // Create project
        ProjectDTO dto = new ProjectDTO();
        dto.setName("Project2");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Fetching the list of projects and get the last ID
        MvcResult getAll = mockMvc.perform(get("/api/projects"))
                .andReturn();

        List<Map<String, Object>> projects = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) (projects.get(projects.size() - 1).get("id"));

        mockMvc.perform(get("/api/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Project2")));
    }

    @Test
    void shouldUpdateProject() throws Exception {
        // Add a new projects
        ProjectDTO dto = new ProjectDTO();
        dto.setName("originalProjectName");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Get ID
        MvcResult getAll = mockMvc.perform(get("/api/projects"))
                .andReturn();

        List<Map<String, Object>> users = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) (users.get(users.size() - 1).get("id"));

        // Update a user
        dto.setName("updatedProjectName");

        mockMvc.perform(put("/api/projects/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Check update result
        mockMvc.perform(get("/api/projects/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updatedProjectName")));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        // Add a project
        ProjectDTO dto = new ProjectDTO();
        dto.setName("projectToDelete");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Get ID
        MvcResult getAll = mockMvc.perform(get("/api/projects"))
                .andReturn();

        List<Map<String, Object>> projects = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) (projects.get(projects.size() - 1).get("id"));

        // Delete
        mockMvc.perform(delete("/api/projects/" + id))
                .andExpect(status().isOk());

        // Check if project is deleted
        mockMvc.perform(get("/api/projects/" + id))
                .andExpect(status().isNotFound());
    }
}
