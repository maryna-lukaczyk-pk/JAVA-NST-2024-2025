package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.Arrays;
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
        Project project1 = new Project();
        project1.setName("Project 1");
        Project project2 = new Project();
        project2.setName("Project 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));
        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        assertEquals("Project 1", projects.get(0).getName());
        assertEquals("Project 2", projects.get(1).getName());

        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID when it exists")
    void testGetProjectById_WhenProjectExists() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Existing Project");
        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.of(existingProject));

        Project result = projectService.getProjectById(projectId);

        assertNotNull(result);
        assertEquals("Existing Project", result.getName());
        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when project does not exist")
    void testGetProjectById_WhenProjectDoesNotExist() {
        Long projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> projectService.getProjectById(projectId));

        assertTrue(exception.getMessage().contains("Project with ID " + projectId + " not found."));
        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject_WhenProjectExists() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old Name");
        Project updatedData = new Project();
        updatedData.setName("New Name");

        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = projectService.updateProject(projectId, updatedData);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        verify(projectRepository).findById(projectId);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when updating non-existing project")
    void testUpdateProject_WhenProjectDoesNotExist() {

        Long projectId = 999L;
        Project updatedData = new Project();
        updatedData.setName("New Name");
        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> projectService.updateProject(projectId, updatedData));

        assertTrue(exception.getMessage().contains("Project with ID " + projectId + " not found."));
        verify(projectRepository).findById(projectId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should delete project by ID when it exists")
    void testDeleteProject_WhenProjectExists() {
        Long projectId = 1L;
        when(projectRepository.existsById(projectId)).thenReturn(true);

        projectService.deleteProject(projectId);

        verify(projectRepository).existsById(projectId);
        verify(projectRepository).deleteById(projectId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when trying to delete non-existing project")
    void testDeleteProject_WhenProjectDoesNotExist() {
        Long projectId = 999L;
        when(projectRepository.existsById(projectId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> projectService.deleteProject(projectId));

        assertTrue(exception.getMessage().contains("Project with ID " + projectId + " does not exist."));
        verify(projectRepository).existsById(projectId);
        verify(projectRepository, never()).deleteById(projectId);
    }
}

