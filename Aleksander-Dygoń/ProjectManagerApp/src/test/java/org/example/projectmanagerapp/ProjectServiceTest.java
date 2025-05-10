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
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("Zwraca wszystkie projekty")
    void testGetAllProjects() {
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Project2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Tworzy nowy projekt")
    void testCreateProject() {
        Project projectToCreate = new Project();
        projectToCreate.setName("NewProject");

        Project savedProject = new Project();
        savedProject.setId(1);
        savedProject.setName("NewProject");

        when(projectRepository.save(projectToCreate)).thenReturn(savedProject);

        Project result = projectService.createProject(projectToCreate);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("NewProject", result.getName());
        verify(projectRepository, times(1)).save(projectToCreate);
    }

    @Test
    @DisplayName("Zwraca ID jezeli projekt istnieje")
    void testGetProjectByIdExists() {
        Project project = new Project();
        project.setId(1);
        project.setName("TestProject");

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1);

        assertTrue(result.isPresent());
        assertEquals("TestProject", result.get().getName());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Zwraca None kiedy nie istnieje")
    void testGetProjectByIdNotExists() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.getProjectById(1);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Usuwa projekt po id")
    void testDeleteProjectById() {
        projectService.deleteProjectById(1);

        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Updateuje projekt jesli istnieje")
    void testUpdateProjectExists() {
        Project existingProject = new Project();
        existingProject.setId(1);
        existingProject.setName("OldName");

        Project projectDetails = new Project();
        projectDetails.setName("NewName");

        Project updatedProject = new Project();
        updatedProject.setId(1);
        updatedProject.setName("NewName");

        when(projectRepository.findById(1)).thenReturn(Optional.of(existingProject));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.updateProject(1, projectDetails);

        assertNotNull(result);
        assertEquals("NewName", result.getName());
        verify(projectRepository, times(1)).findById(1);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    @DisplayName("Zwraca null po próbie update'u nieistniejącego wykładu")
    void testUpdateProjectNotExists() {
        Project projectDetails = new Project();
        projectDetails.setName("NewName");

        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        Project result = projectService.updateProject(1, projectDetails);

        assertNull(result);
        verify(projectRepository, times(1)).findById(1);
        verify(projectRepository, never()).save(any(Project.class));
    }
}