package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void getAllProjects() {
        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("TestProject1");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("TestProject2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new project")
    void createProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.createProject(project);


        assertNotNull(result);
        assertEquals("NewProject", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update an existing project")
    void updateProject_whenProjectExists() {
        long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("UpdatedProject");

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.updateProject(projectId, project);

        assertNotNull(result);
        assertEquals("UpdatedProject", result.getName());
        verify(projectRepository, times(1)).existsById(projectId);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should delete a project")
    void deleteProject() {
        long projectId = 1L;

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }
}