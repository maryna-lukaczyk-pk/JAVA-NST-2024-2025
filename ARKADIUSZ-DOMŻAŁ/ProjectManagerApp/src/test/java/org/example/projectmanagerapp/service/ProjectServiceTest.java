package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    public void testGetAllProjects() {
        Projects project1 = new Projects();
        project1.setName("Project1");

        Projects project2 = new Projects();
        project2.setName("Project2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Projects> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    public void testGetByID() {
        Projects project = new Projects();
        project.setId(1L);
        project.setName("TestProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Projects> result = projectService.getProjectById((1L));

        assertTrue(result.isPresent());
        assertEquals("TestProject", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    public void testCreateProject() {
        Projects newProject = new Projects();
        newProject.setName("NewProject");

        when(projectRepository.save(newProject)).thenReturn(newProject);

        Projects createdProject = projectService.createProject(newProject);

        assertNotNull(createdProject);
        assertEquals("NewProject", createdProject.getName());
        verify(projectRepository, times(1)).save(newProject);
    }

    @Test
    @DisplayName("Should update an existing project")
    public void testUpdateProject() {
        Projects existingProject = new Projects();
        existingProject.setId(1L);
        existingProject.setName("ExistingProject");

        Projects updatedProject = new Projects();
        updatedProject.setName("UpdatedProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Optional<Projects> result = projectService.updateProject(1L, updatedProject);

        assertTrue(result.isPresent());
        assertEquals("UpdatedProject", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should delete an existing project")
    public void testDeleteProject() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        boolean result = projectService.deleteProject(1L);
        assertTrue(result);
        verify(projectRepository, times(1)).deleteById(1L);
    }
}