package com.example.projectmanagerapp;

import com.example.projectmanagerapp.controllers.ProjectController;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProjects() throws Exception {
        Project project1 = new Project();
        project1.setName("Project 1");
        Project project2 = new Project();
        project2.setName("Project 2");

        Mockito.when(projectService.getAll()).thenReturn(Arrays.asList(project1, project2));

        mockMvc.perform(get("/api/projects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        Project project = new Project();
        project.setId(1L);
        project.setName("Project");

        Mockito.when(projectService.getProjectById(1L)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Project"));
    }

    @Test
    void shouldCreateProject() throws Exception {
        Project input = new Project();
        input.setName("Project");
        Project saved = new Project();
        saved.setId(10L);
        saved.setName("Project");

        Mockito.when(projectService.createProject(any(Project.class))).thenReturn(saved);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void shouldUpdateProject() throws Exception {
        Project input = new Project();
        Project updated = new Project();
        updated.setId(1L);
        updated.setName("updated");

        Mockito.when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(updated);

        mockMvc.perform(put("/api/projects/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("updated"));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        Mockito.doNothing().when(projectService).deleteProject(1L);

        mockMvc.perform(delete("/api/projects/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldAssignUserToProject() throws Exception {
        Long projectId = 1L;
        Long userId = 2L;

        mockMvc.perform(post("/api/projects/" + projectId + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("userId", userId))))
                .andExpect(status().isOk());

        Mockito.verify(projectService).assignUserToProject(userId, projectId);
    }

    @Test
    void shouldAssignTaskToProject() throws Exception {
        Long projectId = 1L;
        Long taskId = 3L;

        mockMvc.perform(post("/api/projects/" + projectId + "/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("taskId", taskId))))
                .andExpect(status().isOk());

        Mockito.verify(projectService).assignTaskToProject(taskId, projectId);
    }
}
