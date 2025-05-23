package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    void testGetAllProjects() {
        List<Project> projects = List.of(new Project());
        when(projectRepository.findAll()).thenReturn(projects);

        assertEquals(projects, projectService.getAllProjects());
    }

    @Test
    void testGetProjectById_WhenFound() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertEquals(project, projectService.getProjectById(1L));
    }

    @Test
    void testGetProjectById_WhenNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    void testCreateProject() {
        Project project = new Project();
        when(projectRepository.save(project)).thenReturn(project);

        assertEquals(project, projectService.createProject(project));
    }

    @Test
    void testUpdateProject() {
        Project project = new Project();
        project.setName("Old");

        Project updated = new Project();
        updated.setName("New");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.updateProject(1L, updated);
        assertEquals("New", result.getName());
    }

    @Test
    void testDeleteProject_WhenExists() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void testDeleteProject_WhenNotExists() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> projectService.deleteProject(1L));
    }
}
