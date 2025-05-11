package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() throws Exception {
        // Arrange
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        // Act & Assert
        mockMvc.perform(get("/api/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Project 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Project 2"));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("Should return project by ID when project exists")
    void testGetProjectByIdWhenProjectExists() throws Exception {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("Test Project");

        when(projectService.getProjectById(1L)).thenReturn(Optional.of(project));

        // Act & Assert
        mockMvc.perform(get("/api/project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Project"));

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    @DisplayName("Should return 404 when project does not exist")
    void testGetProjectByIdWhenProjectDoesNotExist() throws Exception {
        // Arrange
        when(projectService.getProjectById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/project/1"))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() throws Exception {
        // Arrange
        Project project = new Project();
        project.setName("New Project");

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("New Project");

        when(projectService.createProject(any(Project.class))).thenReturn(savedProject);

        // Act & Assert
        mockMvc.perform(post("/api/project")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Project\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Project"));

        verify(projectService, times(1)).createProject(any(Project.class));
    }

    @Test
    @DisplayName("Should update project when project exists")
    void testUpdateProjectWhenProjectExists() throws Exception {
        // Arrange
        Project updatedProject = new Project();
        updatedProject.setId(1L);
        updatedProject.setName("Updated Project");

        when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(updatedProject);

        // Act & Assert
        mockMvc.perform(put("/api/project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Project\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Project"));

        verify(projectService, times(1)).updateProject(eq(1L), any(Project.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent project")
    void testUpdateProjectWhenProjectDoesNotExist() throws Exception {
        // Arrange
        when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/project/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Project\"}"))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).updateProject(eq(1L), any(Project.class));
    }

    @Test
    @DisplayName("Should delete project when project exists")
    void testDeleteProjectWhenProjectExists() throws Exception {
        // Arrange
        when(projectService.deleteProject(1L)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/project/1"))
                .andExpect(status().isNoContent());

        verify(projectService, times(1)).deleteProject(1L);
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent project")
    void testDeleteProjectWhenProjectDoesNotExist() throws Exception {
        // Arrange
        when(projectService.deleteProject(1L)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/project/1"))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).deleteProject(1L);
    }
}