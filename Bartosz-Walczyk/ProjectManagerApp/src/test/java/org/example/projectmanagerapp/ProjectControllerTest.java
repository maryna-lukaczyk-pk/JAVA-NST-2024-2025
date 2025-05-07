package org.example.projectmanagerapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.controller.ProjectController;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    @DisplayName("GET /api/projects should return list of projects")
    void testGetAllProjects() throws Exception {
        Project p1 = new Project(); p1.setId(1L); p1.setName("P1");
        Project p2 = new Project(); p2.setId(2L); p2.setName("P2");
        List<Project> projects = Arrays.asList(p1, p2);
        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(projects)));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("GET /api/projects/{id} should return a project when found")
    void testGetProjectByIdFound() throws Exception {
        Project p = new Project(); p.setId(5L); p.setName("FoundProj");
        when(projectService.getProject(5L)).thenReturn(p);

        mockMvc.perform(get("/api/projects/5"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(p)));

        verify(projectService).getProject(5L);
    }

    @Test
    @DisplayName("POST /api/projects should create and return new project")
    void testCreateProject() throws Exception {
        Project input = new Project(); input.setName("NewProj");
        Project saved = new Project(); saved.setId(10L); saved.setName("NewProj");
        when(projectService.createProject(any(Project.class))).thenReturn(saved);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(saved)));

        verify(projectService).createProject(any(Project.class));
    }

    @Test
    @DisplayName("PUT /api/projects should update and return the project")
    void testUpdateProject() throws Exception {
        Project update = new Project(); update.setId(15L); update.setName("UpdatedProj");
        when(projectService.updateProject(any(Project.class))).thenReturn(update);

        mockMvc.perform(put("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(update)));

        verify(projectService).updateProject(any(Project.class));
    }

    @Test
    @DisplayName("DELETE /api/projects should delete and return the project")
    void testDeleteProject() throws Exception {
        Project deleted = new Project(); deleted.setId(20L); deleted.setName("ToDeleteProj");
        when(projectService.deleteProject(eq(20L))).thenReturn(deleted);

        mockMvc.perform(delete("/api/projects")
                        .param("id", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(deleted)));

        verify(projectService).deleteProject(20L);
    }

    @Test
    @DisplayName("DELETE /api/projects should handle null return")
    void testDeleteProjectNotFound() throws Exception {
        when(projectService.deleteProject(eq(99L))).thenReturn(null);

        mockMvc.perform(delete("/api/projects")
                        .param("id", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(projectService).deleteProject(99L);
    }
}
