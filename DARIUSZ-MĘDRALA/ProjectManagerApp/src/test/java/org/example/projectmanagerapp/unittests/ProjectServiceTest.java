package org.example.projectmanagerapp.unittests;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void findAll_shouldReturnAllProjects() {

        Project project1 = new Project();
        project1.setName("Project 1");

        Project project2 = new Project();
        project2.setName("Project 2");

        List<Project> expectedProjects = Arrays.asList(project1, project2);

        when(projectRepository.findAll()).thenReturn(expectedProjects);

        List<Project> actualProjects = projectService.findAll();

        assertEquals(expectedProjects.size(), actualProjects.size());
        assertTrue(actualProjects.contains(project1));
        assertTrue(actualProjects.contains(project2));
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new project")
    void createProject_shouldCreateNewProject() {

        Project project = new Project();
        project.setName("Project 1");

        when(projectRepository.save(project)).thenReturn(project);

        Project createdProject = projectService.createProject(project);

        assertEquals(project, createdProject);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should delete a project")
    void deleteProject_shouldDeleteProject() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));

        Project deletedProject = projectService.deleteProject(1L);

        assertEquals(project, deletedProject);
        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    @DisplayName("Should update a project")
    void updateProject_shouldUpdateProject() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project");

        when(projectRepository.save(updatedProject)).thenReturn(updatedProject);

        Project result = projectService.updateProject(1L, updatedProject);

        assertEquals(updatedProject, result);
        verify(projectRepository, times(1)).save(updatedProject);
    }

    @Test
    @DisplayName("Should find a project by ID")
    void findById_shouldReturnProject() {

        Project project = new Project();
        project.setId(1L);
        project.setName("Project 1");

        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));

        Project foundProject = projectService.findById(1L);

        assertEquals(project, foundProject);
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found")
    void findById_shouldThrowExceptionWhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            projectService.findById(1L);
        } catch (RuntimeException e) {
            assertEquals("Project not found", e.getMessage());
        }

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found on delete")
    void deleteProject_shouldThrowException() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            projectService.deleteProject(1L);
        } catch (RuntimeException e) {
            assertEquals("Project not found", e.getMessage());
        }

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found on update")
    void updateProject_shouldThrowException() {
        when(projectRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        try {
            projectService.updateProject(1L, new Project());
        } catch (RuntimeException e) {
            assertEquals("Project not found", e.getMessage());
        }

        verify(projectRepository, times(1)).findById(1L);

    }
}
