package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
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
        Project project1 = new Project();
        project1.setName("Project A");

        Project project2 = new Project();
        project2.setName("Project B");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();
        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list of projects")
    void testGetAllProjectsEmpty() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        List<Project> projects = projectService.getAllProjects();
        assertEquals(0, projects.size());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Should create new project")
    void testCreateProject() {
        Project project = new Project();
        project.setName("New Project");

        when(projectRepository.save(project)).thenReturn(project);

        Project created = projectService.createProject(project);
        assertEquals("New Project", created.getName());
        verify(projectRepository).save(project);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        long id = 1L;

        doNothing().when(projectRepository).deleteById(id);

        projectService.deleteProject(id);
        verify(projectRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject() {
        long id = 2L;
        Project existing = new Project();
        existing.setName("Old");

        Project updated = new Project();
        updated.setName("Updated");

        when(projectRepository.findById(id)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(id, updated);
        assertEquals("Updated", result.getName());
        verify(projectRepository).findById(id);
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("Should throw when updating non-existent project")
    void testUpdateProjectNotFound() {
        long id = 3L;
        Project updated = new Project();
        updated.setName("Updated");

        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(id, updated);
        });

        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository).findById(id);
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        long id = 4L;
        Project project = new Project();
        project.setName("Found");

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(id);
        assertEquals("Found", result.getName());
        verify(projectRepository).findById(id);
    }

    @Test
    @DisplayName("Should throw when project not found by ID")
    void testGetProjectByIdNotFound() {
        long id = 5L;

        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            projectService.getProjectById(id);
        });

        assertEquals("Project not found", exception.getMessage());
        verify(projectRepository).findById(id);
    }
}
