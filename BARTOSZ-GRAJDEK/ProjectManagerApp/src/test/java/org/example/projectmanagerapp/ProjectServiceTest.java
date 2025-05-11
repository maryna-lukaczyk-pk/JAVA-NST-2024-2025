package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));

        projectService.getAllProjects();
        verify(projectRepository).findAll();
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project()));

        projectService.getProjectById(1L);
        verify(projectRepository).findById(1L);
    }

    @Test
    void testCreateProject() {
        Project project = new Project();

        when(projectRepository.save(project)).thenReturn(project);

        projectService.createProject(project);
        verify(projectRepository).save(project);
    }

    @Test
    void testUpdateProject() {
        Project existing = new Project("Old Name");
        Project updated = new Project("New Name");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));

        projectService.updateProject(1L, updated);
        verify(projectRepository).save(existing);
    }

    @Test
    void testDeleteProject() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }
}
