package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.config.TestConfig;
import com.example.projectmanagerapp.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class ProjectCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateProject() throws Exception {
        Project project = new Project();
        project.setName("testCreateProject");

        String projectJson = objectMapper.writeValueAsString(project);
        mockMvc.perform(post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("testCreateProject")));
    }

    @Test
    public void testGetAllProjects() throws Exception {
        Project project = new Project();
        project.setName("testGetAllProject");

        String projectJson = objectMapper.writeValueAsString(project);
        mockMvc.perform(post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/projects/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].name", hasItem("testGetAllProject")));
    }

    @Test
    public void testUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("testUpdateProjectBefore");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult result = mockMvc.perform(post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project.class);

        createdProject.setName("testUpdateProjectAfter");
        String updatedProjectJson = objectMapper.writeValueAsString(createdProject);
        mockMvc.perform(put("/api/projects/update/" + createdProject.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedProjectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testUpdateProjectAfter")));
    }

    @Test
    public void testDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("testDeleteProject");

        String projectJson = objectMapper.writeValueAsString(project);
        MvcResult result = mockMvc.perform(post("/api/projects/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
                .andExpect(status().isCreated())
                .andReturn();

        Project createdProject = objectMapper.readValue(
                result.getResponse().getContentAsString(), Project.class);

        mockMvc.perform(delete("/api/projects/delete/" + createdProject.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", not(hasItem("testDeleteProject"))));
    }
}
