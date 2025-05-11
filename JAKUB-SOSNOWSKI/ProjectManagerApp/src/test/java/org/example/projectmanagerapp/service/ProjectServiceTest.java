package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    public ProjectServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProjects_shouldReturnAllProjects() {
        List<Project> mockProjects = Arrays.asList(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(mockProjects);

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
    }

    @Test
    void getProjectById_shouldReturnProject() {
        Project project = new Project();
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
    }

    @Test
    void createProject_shouldSaveAndReturnProject() {
        Project project = new Project();
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);

        assertEquals(project, result);
    }

    @Test
    void updateProject_shouldUpdateAndReturnProject() {
        Project oldProject = new Project();
        oldProject.setName("Old");

        Project newProject = new Project();
        newProject.setName("New");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(oldProject));
        when(projectRepository.save(any(Project.class))).thenReturn(newProject);

        Project result = projectService.updateProject(1L, newProject);

        assertEquals("New", result.getName());
    }

    @Test
    void deleteProject_shouldCallDeleteById() {
        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }
}
