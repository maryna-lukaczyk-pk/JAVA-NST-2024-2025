package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

    private ProjectService projectService;
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        projectService = Mockito.mock(ProjectService.class);
        projectController = new ProjectController();
        // Wstrzykujemy mockowany service do controllera przez refleksję
        try {
            java.lang.reflect.Field projectServiceField = ProjectController.class.getDeclaredField("projectService");
            projectServiceField.setAccessible(true);
            projectServiceField.set(projectController, projectService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllProjects() {
        Project project1 = new Project("Project1", "Desc1");
        project1.setId(1L);

        Project project2 = new Project("Project2", "Desc2");
        project2.setId(2L);

        when(projectService.getAllProjects()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectController.getAllProjects();

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertEquals("Project1", projects.get(0).getName());

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void testGetProjectByIdFound() {
        Project project = new Project("Project1", "Desc1");
        project.setId(1L);

        when(projectService.getProjectById(1L)).thenReturn(Optional.of(project));

        ResponseEntity<Project> response = projectController.getProjectById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Project1", response.getBody().getName());

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void testGetProjectByIdNotFound() {
        when(projectService.getProjectById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Project> response = projectController.getProjectById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void testCreateProject() {
        Project inputProject = new Project("New Project", "New Desc");

        Project savedProject = new Project("New Project", "New Desc");
        savedProject.setId(1L);

        when(projectService.createProject(any(Project.class))).thenReturn(savedProject);

        Project result = projectController.createProject(inputProject);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Project", result.getName());

        verify(projectService, times(1)).createProject(inputProject);
    }

    @Test
    void testUpdateProjectFound() {
        Long id = 1L;
        Project inputProject = new Project("Updated Project", "Updated Desc");

        Project updatedProject = new Project("Updated Project", "Updated Desc");
        updatedProject.setId(id);

        when(projectService.updateProject(eq(id), any(Project.class))).thenReturn(updatedProject);

        ResponseEntity<Project> response = projectController.updateProject(id, inputProject);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Project", response.getBody().getName());

        verify(projectService, times(1)).updateProject(id, inputProject);
    }

    @Test
    void testUpdateProjectNotFound() {
        Long id = 1L;
        Project inputProject = new Project("Updated Project", "Updated Desc");

        when(projectService.updateProject(eq(id), any(Project.class))).thenReturn(null);

        ResponseEntity<Project> response = projectController.updateProject(id, inputProject);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(projectService, times(1)).updateProject(id, inputProject);
    }

    @Test
    void testDeleteProjectFound() {
        Long id = 1L;

        when(projectService.getProjectById(id)).thenReturn(Optional.of(new Project()));

        ResponseEntity<Void> response = projectController.deleteProject(id);

        assertEquals(204, response.getStatusCodeValue());

        verify(projectService, times(1)).getProjectById(id);
        verify(projectService, times(1)).deleteProject(id);
    }

    @Test
    void testDeleteProjectNotFound() {
        Long id = 1L;

        when(projectService.getProjectById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = projectController.deleteProject(id);

        assertEquals(404, response.getStatusCodeValue());

        verify(projectService, times(1)).getProjectById(id);
        verify(projectService, never()).deleteProject(anyLong());
    }

}