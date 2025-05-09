package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
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
    void getAllProjects() {
        Project p1 = new Project("Project A", "Desc A", "ACTIVE");
        Project p2 = new Project("Project B", "Desc B", "PLANNED");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void getProjectById() {
        Project p = new Project("Demo", "Testing", "ACTIVE");
        p.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));

        Project result = projectService.getProjectById(1L);

        assertEquals("Demo", result.getName());
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create project")
    void createProject() {
        Project p = new Project("ProjX", "Important", "PLANNED");

        when(projectRepository.save(p)).thenReturn(p);

        Project saved = projectService.createProject(p);

        assertEquals("ProjX", saved.getName());
        verify(projectRepository).save(p);
    }

    @Test
    @DisplayName("Should update project")
    void updateProject() {
        Project existing = new Project("OldName", "OldDesc", "PLANNED");
        existing.setId(1L);

        Project updated = new Project("NewName", "NewDesc", "ACTIVE");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(1L, updated);

        assertEquals("NewName", result.getName());
        assertEquals("NewDesc", result.getDescription());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("Should delete project")
    void deleteProject() {
        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }
}
