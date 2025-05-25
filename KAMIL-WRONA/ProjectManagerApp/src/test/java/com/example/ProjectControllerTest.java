package com.example.controller;

import com.example.entity.Project;
import com.example.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProjects() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setName("Mock Project");
        project.setDescription("Mock Desc");

        when(projectService.getAllProjects()).thenReturn(Collections.singletonList(project));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mock Project"));
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");
        project.setDescription("Test Description");

        when(projectService.findById(1L)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    @Test
    void shouldCreateProject() throws Exception {
        Project project = new Project();
        project.setName("New Project");
        project.setDescription("New Desc");

        when(projectService.save(any(Project.class))).thenReturn(project);

        mockMvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void shouldUpdateProject() throws Exception {
        Project updated = new Project();
        updated.setId(1L);
        updated.setName("Updated Project");
        updated.setDescription("Updated Desc");

        when(projectService.update(eq(1L), any(Project.class))).thenReturn(updated);

        mockMvc.perform(put("/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        mockMvc.perform(delete("/projects/1"))
                .andExpect(status().isOk());

        verify(projectService, times(1)).delete(1L);
    }

    @Test
    void shouldAssignUsersToProject() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setName("Assigned");

        when(projectService.assignUsers(eq(1L), anyList())).thenReturn(project);

        mockMvc.perform(put("/projects/1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonList(2L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Assigned"));
    }
}
