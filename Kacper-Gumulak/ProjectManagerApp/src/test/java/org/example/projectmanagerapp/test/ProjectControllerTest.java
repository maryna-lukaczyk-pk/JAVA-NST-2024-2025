package org.example.projectmanagerapp.test;

import org.example.projectmanagerapp.controller.ProjectController;
import org.example.projectmanagerapp.dto.ProjectDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Testy związane z kontrolerem zadania
class ProjectControllerTest {

    private ProjectService projectService;
    private ProjectController projectController;

    @BeforeEach
    void setUp() {
        projectService = Mockito.mock(ProjectService.class);
        projectController = new ProjectController(projectService);
    }

    private ProjectDTO createFakeDTO(Long id) {
        return new ProjectDTO(id, "Project " + id, List.of(1L, 2L), List.of(3L, 4L));
    }

    @Test
    void testGetAllProjects() {
        List<ProjectDTO> expectedProjects = List.of(createFakeDTO(1L), createFakeDTO(2L) );

        when(projectService.getAllProjects()).thenReturn(expectedProjects);

        List<ProjectDTO> result = projectController.getAllProjects();

        assertEquals(expectedProjects, result);
        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    void testGetProjectById() {
        ProjectDTO expected = createFakeDTO(1L);
        when(projectService.getProjectById(1L)).thenReturn(expected);

        ProjectDTO result = projectController.getProjectById(1L);

        assertEquals(expected, result);
        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    void testCreateProject() {
        Project input = new Project("New Project");
        ProjectDTO expected = createFakeDTO(3L);
        when(projectService.createProject(input)).thenReturn(expected);

        ProjectDTO result = projectController.createProject(input);

        assertEquals(expected, result);
        verify(projectService, times(1)).createProject(input);
    }

    @Test
    void testUpdateProject() {
        Project input = new Project("Changed Project");
        ProjectDTO expected = createFakeDTO(4L);
        when(projectService.updateProject(4L, input)).thenReturn(expected);

        ProjectDTO result = projectController.updateProject(4L, input);

        assertEquals(expected, result);
        verify(projectService, times(1)).updateProject(4L, input);
    }

    @Test
    void testDeleteProject() {
        doNothing().when(projectService).deleteProject(5L);
        projectController.deleteProject(5L);
        verify(projectService, times(1)).deleteProject(5L);
    }
}