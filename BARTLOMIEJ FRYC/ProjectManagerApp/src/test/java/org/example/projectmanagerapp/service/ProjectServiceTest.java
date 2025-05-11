package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
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

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService();

        // Use reflection to set the repository
        try {
            java.lang.reflect.Field field = ProjectService.class.getDeclaredField("projectRepository");
            field.setAccessible(true);
            field.set(projectService, projectRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        // Arrange
        Project project1 = new Project();
        project1.setName("TestProject1");

        Project project2 = new Project();
        project2.setName("TestProject2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        // Act
        List<Project> projects = projectService.getAllProjects();

        // Assert
        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById() {
        // Arrange
        Project project = new Project();
        project.setId(1L);
        project.setName("TestProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        // Act
        Optional<Project> result = projectService.getProjectById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestProject", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create project")
    void testCreateProject() {
        // Arrange
        Project project = new Project();
        project.setName("NewProject");

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Project result = projectService.createProject(project);

        // Assert
        assertEquals("NewProject", result.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update project name only")
    void testUpdateProjectNameOnly() {
        // Arrange
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("OldName");

        Project updatedDetails = new Project();
        updatedDetails.setName("NewName");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = projectService.updateProject(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("NewName", result.getName());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should update project tasks only")
    void testUpdateProjectTasksOnly() {
        // Arrange
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("OldName");

        Project updatedDetails = new Project();
        updatedDetails.setTasks(new java.util.HashSet<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = projectService.updateProject(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldName", result.getName());
        assertNotNull(result.getTasks());
        assertTrue(result.getTasks().isEmpty());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should update project users only")
    void testUpdateProjectUsersOnly() {
        // Arrange
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("OldName");

        Project updatedDetails = new Project();
        updatedDetails.setUsers(new java.util.HashSet<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = projectService.updateProject(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldName", result.getName());
        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().isEmpty());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should update all project fields")
    void testUpdateAllProjectFields() {
        // Arrange
        Project existingProject = new Project();
        existingProject.setId(1L);
        existingProject.setName("OldName");

        Project updatedDetails = new Project();
        updatedDetails.setName("NewName");
        updatedDetails.setTasks(new java.util.HashSet<>());
        updatedDetails.setUsers(new java.util.HashSet<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Project result = projectService.updateProject(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("NewName", result.getName());
        assertNotNull(result.getTasks());
        assertTrue(result.getTasks().isEmpty());
        assertNotNull(result.getUsers());
        assertTrue(result.getUsers().isEmpty());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    @DisplayName("Should return null when updating non-existent project")
    void testUpdateNonExistentProject() {
        // Arrange
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Project result = projectService.updateProject(1L, new Project());

        // Assert
        assertNull(result);
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Should delete project")
    void testDeleteProject() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = projectService.deleteProject(1L);

        // Assert
        assertTrue(result);
        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent project")
    void testDeleteNonExistentProject() {
        // Arrange
        when(projectRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = projectService.deleteProject(1L);

        // Assert
        assertFalse(result);
        verify(projectRepository, times(1)).existsById(1L);
        verify(projectRepository, never()).deleteById(any());
    }
}
