package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        project1.setName("TestProject1");

        Project project2 = new Project();
        project2.setName("TestProject2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        Project project = new Project();
        project.setName("TestProject3");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.projectById(1);

        assertEquals("TestProject3", result.getName());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should return null if project by ID not found")
    void testGetProjectByIdNotFound() {
        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        Project result = projectService.projectById(99);

        assertNull(result);
        verify(projectRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Should add new project")
    void testAddProject() {
        Project project = new Project();
        project.setName("TestProject4");

        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.addProject(project);

        assertEquals("TestProject4", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() {
        Project existing = new Project();
        existing.setName("TestProject5");

        Project updated = new Project();
        updated.setName("TestProject6");

        when(projectRepository.findById(1)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(1, updated);

        assertEquals("TestProject6", result.getName());
        verify(projectRepository, times(1)).findById(1);
        verify(projectRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should return null if project to update does not exist")
    void testUpdateProjectNotFound() {
        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        Project updateRequest = new Project();
        updateRequest.setName("AttemptedUpdate");

        Project result = projectService.updateProject(99, updateRequest);

        assertNull(result);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject() {
        when(projectRepository.existsById(1)).thenReturn(true);

        projectService.deleteProject(1);

        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should not delete project if it does not exist")
    void testDeleteProjectNotFound() {
        when(projectRepository.existsById(99)).thenReturn(false);

        projectService.deleteProject(99);

        verify(projectRepository, times(1)).existsById(99);
        verify(projectRepository, never()).deleteById(any());
    }
}
