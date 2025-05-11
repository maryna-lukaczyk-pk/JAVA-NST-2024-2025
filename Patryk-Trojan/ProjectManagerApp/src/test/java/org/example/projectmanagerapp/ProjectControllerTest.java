package org.example.projectmanagerapp;

import org.example.projectmanagerapp.controller.ProjectController;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProjectControllerTest {
    private ProjectController projectController;
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        projectService = Mockito.mock(ProjectService.class);
        projectController = new ProjectController(projectService);
    }

    @Test
    @DisplayName("GET /api/tasks - Should return all projects")
    public void shouldReturnAllProjects() {
        // Given
        Project project1 = new Project();
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setName("Project 2");

        List<Project> projects = Arrays.asList(project1, project2);
        when(projectService.getAllProjects()).thenReturn(projects);

        // When
        List<Project> result = projectController.getAllProjects();

        // Then
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).getName());
        assertEquals("Project 2", result.get(1).getName());
        verify(projectService, times(1)).getAllProjects();
    }
    @Test
    @DisplayName("POST /api/tasks - Should create new project")
    public void shouldCreateProject() {
        // Given
        Project inputProject = new Project();
        inputProject.setName("New Project");

        Project savedProject = new Project();
        savedProject.setId(1L);
        savedProject.setName("New Project");

        when(projectService.createProject(inputProject)).thenReturn(savedProject);

        // When
        Project result = projectController.createProject(inputProject);

        // Then
        assertEquals(1L, result.getId());
        assertEquals("New Project", result.getName());
        verify(projectService, times(1)).createProject(inputProject);
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - Should delete project")
    public void shouldDeleteProject() {
        // Given
        doNothing().when(projectService).deleteProject(1L);

        // When
        projectController.deleteProject(1L);

        // Then
        verify(projectService, times(1)).deleteProject(1L);
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} - Should update project")
    public void shouldUpdateProject() {
        // Given
        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");
        doNothing().when(projectService).updateProject(1L, updatedProject);

        // When
        projectController.updateProject(updatedProject, 1L);

        // Then
        verify(projectService, times(1)).updateProject(1L, updatedProject);
    }
}
