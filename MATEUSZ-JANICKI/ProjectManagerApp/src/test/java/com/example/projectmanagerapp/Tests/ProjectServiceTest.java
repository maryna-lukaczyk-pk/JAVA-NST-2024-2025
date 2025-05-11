package com.example.projectmanagerapp.Tests;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {
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
        Project project1 = new Project();
        project1.setName("Project Alpha");

        Project project2 = new Project();
        project2.setName("Project Beta");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a project")
    void testCreateProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);

        assertEquals("NewProject", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should find project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("FoundProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals("FoundProject", result.get().getName());
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProjectById() {
        projectService.deleteProjectById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }


    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("OldName");

        Project updatedProject = new Project();
        updatedProject.setName("NewName");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(inv -> inv.getArgument(0));

        Project result = projectService.updateProject(1L, updatedProject);

        assertEquals("NewName", result.getName());
    }
}
