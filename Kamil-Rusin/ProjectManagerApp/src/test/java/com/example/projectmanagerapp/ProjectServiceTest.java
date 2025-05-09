package com.example.projectmanagerapp;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setName("Project 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAll();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Sample Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sample Project", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project project = new Project();
        project.setName("New Project");

        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectService.createProject(project);

        assertEquals("New Project", savedProject.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateExistingProject() {
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old");

        Project updated = new Project();
        updated.setName("Updated");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.updateProject(1L, updated);

        assertEquals("Updated", result.getName());
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("Should save new project when updating non-existing one")
    void testUpdateNonExistingProject() {
        Project project = new Project();
        project.setName("Example");

        when(projectRepository.findById(5L)).thenReturn(Optional.empty());
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.updateProject(5L, project);

        assertEquals("Example", result.getName());
        verify(projectRepository).save(project);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        projectService.deleteProject(2L);
        verify(projectRepository).deleteById(2L);
    }
}
