package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.CreateProjectRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class ProjectServiceTests {

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
        Project p1 = new Project();
        p1.setName("Test Project 1");

        Project p2 = new Project();
        p2.setName("Test Project 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw when no projects found")
    void testGetAllProjectsThrowsWhenEmpty() {
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ResponseStatusException.class, () -> projectService.getAllProjects());
    }

    @Test
    @DisplayName("Should get project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setName("Test Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertEquals("Test Project", result.getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw when project ID not found")
    void testGetProjectByIdThrows() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    @DisplayName("Should create new project")
    void testCreateProject() {
        CreateProjectRequest request = new CreateProjectRequest("New Project");

        Project toSave = new Project();
        toSave.setName("New Project");

        Project saved = new Project();
        saved.setId(1L);
        saved.setName("New Project");

        when(projectRepository.save(any(Project.class))).thenReturn(saved);

        Project result = projectService.createProject(request);

        assertEquals("New Project", result.getName());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject() {
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old Name");

        CreateProjectRequest request = new CreateProjectRequest("Updated Name");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project updated = projectService.updateProject(1L, request);

        assertEquals("Updated Name", updated.getName());
        verify(projectRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        projectService.deleteProject(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }
}

