package org.example.projectmanagerapp;

import org.example.projectmanagerapp.controllers.ProjectController;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectControllerTest {

    private ProjectService projectService;
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        projectService = mock(ProjectService.class);
        projectController = new ProjectController(projectService);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setName("Project2");

        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectController.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1);
        project.setName("TestProject");

        when(projectService.getProjectById(1)).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
    }

    @Test
    @DisplayName("Should return 404 when project not found")
    void testGetProjectByIdNotFound() {
        when(projectService.getProjectById(999)).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.getProjectById(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should create new project")
    void testCreateProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectService.createProject(project)).thenReturn(project);

        ResponseEntity<Project> response = projectController.createProject(project);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(project, response.getBody());
        verify(projectService, times(1)).createProject(project);
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() {
        Project project = new Project();
        project.setName("UpdatedProject");

        when(projectService.updateProject(project)).thenReturn(project);

        ResponseEntity<Project> response = projectController.updateProject(1, project);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(project, response.getBody());
        verify(projectService, times(1)).updateProject(project);
    }

    @Test
    @DisplayName("Should delete project")
    void testDeleteProject() {
        ResponseEntity<Void> response = projectController.deleteProject(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(projectService, times(1)).deleteProject(1);
    }
}