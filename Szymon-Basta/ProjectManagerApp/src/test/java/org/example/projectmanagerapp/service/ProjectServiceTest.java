package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Projects p1 = new Projects();
        p1.setName("Project 1");

        Projects p2 = new Projects();
        p2.setName("Project 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Projects> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        Projects project = new Projects();
        project.setName("Demo Project");

        when(projectRepository.findById(2L)).thenReturn(Optional.of(project));

        Projects result = projectService.getProjectById(2L);

        assertNotNull(result);
        assertEquals("Demo Project", result.getName());
        verify(projectRepository, times(1)).findById(2L);
    }
}
