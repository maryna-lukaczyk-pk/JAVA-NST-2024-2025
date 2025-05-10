package com.example.demo.service;

import com.example.demo.entity.Project;
import com.example.demo.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        assertEquals("TestProject1", result.get(0).getName());
        assertEquals("TestProject2", result.get(1).getName());

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

    }

    @Test
    @DisplayName("Should update an existing project")
    void updateProject() {
        Long projectId = 1L;
        Project updatedProject = new Project();
        updatedProject.setName("UpdatedProject");

        when(projectRepository.existsById(projectId)).thenReturn(true);
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.updateProject(projectId, updatedProject);

        assertNotNull(result);
        assertEquals("UpdatedProject", result.getName());
        assertEquals(projectId, updatedProject.getId());

    }


    @Test
    @DisplayName("Should delete a project")
    void deleteProject() {
        Long projectId = 1L;

        projectService.deleteProject(projectId);

    }
}