package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repositories.ProjectRepository;
import org.example.projectmanagerapp.services.ProjectService;
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
        Project project1 = new Project();
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setName("Project2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create project")
    void testCreateProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectRepository.save(project)).thenReturn(project);

        Project created = projectService.createProject(project);

        assertNotNull(created);
        assertEquals("NewProject", created.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should find project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1);
        project.setName("Project1");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Optional<Project> found = projectService.getProjectById(1);

        assertTrue(found.isPresent());
        assertEquals("Project1", found.get().getName());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        projectService.deleteProject(1);

        verify(projectRepository, times(1)).deleteById(1);
    }
}