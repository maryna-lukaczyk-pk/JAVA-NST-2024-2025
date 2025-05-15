package org.example.projectmanagerapp.integration;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ProjectControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testCreateAndGetProject() throws Exception {
        Project project = new Project();
        project.setName("Test Project");

        String projectJson = objectMapper.writeValueAsString(project);

        String responseContent = mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Project createdProject = objectMapper.readValue(responseContent, Project.class);

        mockMvc.perform(get("/projects/" + createdProject.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Project"));
    }

    @Test
    public void testUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("Original Name");
        Project savedProject = projectRepository.save(project);

        Project update = new Project();
        update.setName("Updated Name");

        mockMvc.perform(put("/projects/" + savedProject.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/projects/" + savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Name"));
    }

    @Test
    public void testDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("To Be Deleted");
        Project savedProject = projectRepository.save(project);

        mockMvc.perform(delete("/projects/" + savedProject.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/projects/" + savedProject.getId()))
                .andExpect(status().isNotFound());

        assertFalse(projectRepository.findById(savedProject.getId()).isPresent());
    }
}