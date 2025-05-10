
package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        p1.setName("Proj1");
        Projects p2 = new Projects();
        p2.setName("Proj2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Projects> projects = projectService.getAllProjects();
        assertEquals(2, projects.size());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Should create project")
    void testCreateProject() {
        Projects project = new Projects();
        project.setName("New");

        when(projectRepository.save(project)).thenReturn(project);

        Projects result = projectService.createProject(project);
        assertEquals("New", result.getName());
        verify(projectRepository).save(project);
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() {
        Projects old = new Projects();
        old.setName("Old");

        Projects updated = new Projects();
        updated.setName("New");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(old));
        when(projectRepository.save(old)).thenReturn(old);

        Projects result = projectService.updateProject(1L, updated);
        assertEquals("New", result.getName());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(old);
    }

    @Test
    @DisplayName("Should delete project")
    void testDeleteProject() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should get project by ID")
    void testGetProjectById() {
        Projects project = new Projects();
        project.setId(1L);
        project.setName("Project1");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Projects result = projectService.getProjectById(1L);
        assertEquals("Project1", result.getName());
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw error project not found by ID")
    void testGetProjectByIdThrows() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> projectService.getProjectById(99L));
    }

    @Test
    @DisplayName("Should throw error deleting non-existent project")
    void testDeleteProjectThrows() {
        when(projectRepository.existsById(99L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> projectService.deleteProject(99L));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent project")
    void testUpdateProjectThrows() {
        Projects updated = new Projects();
        updated.setName("ShouldFail");
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> projectService.updateProject(99L, updated));
    }

}
