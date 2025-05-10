package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project p1 = new Project(); p1.setName("P1");
        Project p2 = new Project(); p2.setName("P2");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> res = projectService.getAllProjects();

        assertEquals(2, res.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get project by ID")
    void testGetProjectById() {
        Project p = new Project(); p.setId(10L); p.setName("X");
        when(projectRepository.findById(10L)).thenReturn(Optional.of(p));

        Project res = projectService.getProjectById(10L);

        assertEquals("X", res.getName());
        verify(projectRepository).findById(10L);
    }

    @Test
    @DisplayName("Should create project")
    void testCreateProject() {
        Project p = new Project(); p.setName("New");
        when(projectRepository.save(p)).thenReturn(p);

        Project res = projectService.createProject(p);

        assertSame(p, res);
        verify(projectRepository).save(p);
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() {
        Project existing = new Project(); existing.setId(5L); existing.setName("Old");
        Project dto      = new Project(); dto.setName("New");
        when(projectRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project res = projectService.updateProject(5L, dto);

        assertEquals("New", res.getName());
        verify(projectRepository).findById(5L);
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete project")
    void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(7L);
        projectService.deleteProject(7L);
        verify(projectRepository).deleteById(7L);
    }
}
