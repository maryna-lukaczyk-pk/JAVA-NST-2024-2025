package org.example.projectmanager.service;

import org.example.projectmanager.entity.Project;
import org.example.projectmanager.repository.ProjectRepository;
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
    public void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    public void testGetAllProjects() {
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
    @DisplayName("Should return project by ID")
    public void testGetByID() {
        Project project = new Project();
        project.setId(1L);
        project.setName("TestProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestProject", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    public void testCreateProject() {
        Project newProject = new Project();
        newProject.setName("NewProject");

        when(projectRepository.save(newProject)).thenReturn(newProject);

        Project createdProject = projectService.createProject(newProject);

        assertNotNull(createdProject);
        assertEquals("NewProject", createdProject.getName());
        verify(projectRepository, times(1)).save(newProject);
    }

    @Test
    @DisplayName("Should update an existing project")
    public void testUpdateProject() {
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("ExistingProject");

        Project updatedProject = new Project();
        updatedProject.setName("UpdatedProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(existingProject)).thenReturn(existingProject);

        Optional<Project> result = Optional.ofNullable(projectService.updateProject(1L, updatedProject));

        assertTrue(result.isPresent());
        assertEquals("UpdatedProject", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should delete an existing project")
    public void testDeleteProject() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        projectService.deleteProject(1L);
        verify(projectRepository, times(1)).delete(project);
    }
}