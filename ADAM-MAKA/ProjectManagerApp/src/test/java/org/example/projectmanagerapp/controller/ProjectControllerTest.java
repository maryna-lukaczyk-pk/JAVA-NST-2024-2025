package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    private ProjectService projectService;
    private ProjectController projectController;

    @BeforeEach
    void setUp() throws Exception {
        projectService = Mockito.mock(ProjectService.class);
        projectController = new ProjectController(projectService);

        java.lang.reflect.Field field = ProjectController.class.getDeclaredField("projectService");
        field.setAccessible(true);
        field.set(projectController, projectService);
    }

    @Test
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Project 2");

        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectController.getAllProjects();

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertEquals("Project 1", projects.get(0).getName());

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void testGetProjectByIdExists() {
        Project project = new Project();
        project.setId(1);
        project.setName("Existing Project");

        when(projectService.getProjectById(1)).thenReturn(project);

        ResponseEntity<Project> response = projectController.getProjectById(1);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Existing Project", Objects.requireNonNull(response.getBody()).getName());

        verify(projectService, times(1)).getProjectById(1);
    }

    @Test
    void testGetProjectByIdNotFound() {
        when(projectService.getProjectById(100)).thenReturn(null);

        ResponseEntity<Project> response = projectController.getProjectById(100);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        verify(projectService, times(1)).getProjectById(100);
    }

    @Test
    void testCreateProject() {
        Project inputProject = new Project();
        inputProject.setName("New Project");

        Project createdProject = new Project();
        createdProject.setId(1);
        createdProject.setName("New Project");

        when(projectService.createProject(inputProject)).thenReturn(createdProject);

        Project result = projectController.createProject(inputProject);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("New Project", result.getName());

        verify(projectService, times(1)).createProject(inputProject);
    }

    @Test
    void testUpdateProject() {
        Project updatedData = new Project();
        updatedData.setName("Updated Project");

        Project updatedProject = new Project();
        updatedProject.setId(1);
        updatedProject.setName("Updated Project");

        when(projectService.updateProject(1, updatedData)).thenReturn(updatedProject);

        Project result = projectController.updateProject(1, updatedData);
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Updated Project", result.getName());

        verify(projectService, times(1)).updateProject(1, updatedData);
    }

    @Test
    void testDeleteProject() {
        projectController.deleteProject(1);
        verify(projectService, times(1)).deleteProject(1);
    }
}