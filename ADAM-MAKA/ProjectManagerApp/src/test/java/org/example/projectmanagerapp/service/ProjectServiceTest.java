package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        projectService = new ProjectService(projectRepository, userRepository);
    }

    @Test
    @DisplayName("Test getAllProjects() returns all projects")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setId(1);

        Project project2 = new Project();
        project2.setId(2);

        when(projectRepository.findAll()).thenReturn(java.util.Arrays.asList(project1, project2));

        java.util.List<Project> projects = projectService.getAllProjects();
        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getProjectById() returns the project when found")
    void testGetProjectByIdFound() {
        Project project = new Project();
        project.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1);
        assertEquals(project.getId(), result.getId());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test getProjectById() returns null when project not found")
    void testGetProjectByIdNotFound() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Project result = projectService.getProjectById(1);
        assertNull(result);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test createProject() saves and returns the project")
    void testCreateProject() {
        Project project = new Project();
        project.setId(1);

        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.createProject(project);
        assertEquals(project.getId(), result.getId());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Test updateProject() updates and returns the project when found")
    void testUpdateProjectFound() {
        Project existingProject = new Project();
        existingProject.setId(1);

        Project updatedProject = new Project();
        updatedProject.setName("Updated project");
        updatedProject.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(updatedProject)).thenReturn(updatedProject);

        Project result = projectService.updateProject(1, updatedProject);
        assertEquals("Updated project", result.getName());
        verify(projectRepository, times(1)).findById(1);
        verify(projectRepository, times(1)).save(updatedProject);
    }

    @Test
    @DisplayName("Test updateProject() returns null when project is not found")
    void testUpdateProjectNotFound() {
        Project updatedProject = new Project();
        updatedProject.setName("Updated project");

        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Project result = projectService.updateProject(1, updatedProject);
        assertNull(result);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Test deleteProject() calls deleteById() method")
    void testDeleteProject() {
        projectService.deleteProject(1);
        verify(projectRepository, times(1)).deleteById(1);
    }
}