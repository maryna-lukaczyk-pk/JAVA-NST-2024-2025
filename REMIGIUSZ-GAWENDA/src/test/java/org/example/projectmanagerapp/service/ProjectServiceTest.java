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
        Project p1 = new Project("Proj1", "Desc1", "ACTIVE");
        Project p2 = new Project("Proj2", "Desc2", "INACTIVE");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void getProjectById() {
        Project p = new Project("Test", "Desc", "ACTIVE");
        p.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));

        Project result = projectService.getProjectById(1L);

        assertEquals("Test", result.getName());
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("Should save new project")
    void createProject() {
        Project p = new Project("ProjX", "Some desc", "PLANNED");

        when(projectRepository.save(p)).thenReturn(p);

        Project result = projectService.createProject(p);

        assertEquals("ProjX", result.getName());
        verify(projectRepository).save(p);
    }
}
