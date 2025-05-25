package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ProjectRepository projectRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository, userService);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("Project A");

        Project p2 = new Project();
        p2.setId(2L);
        p2.setName("Project B");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        assertEquals("Project A", result.getFirst().getName());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Should return project by ID when it exists")
    void testGetProjectById_WhenExists() {
        Project project = new Project();
        project.setId(1L);
        project.setName("Example Project");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals("Example Project", result.getName());
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when project not found")
    void testGetProjectById_WhenNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            projectService.getProjectById(99L);
        });

        assertEquals("Project not found", ex.getMessage());
        verify(projectRepository).findById(99L);
    }

    @Test
    @DisplayName("Should create a new project")
    void testCreateProject() {
        Project toCreate = new Project();
        toCreate.setName("Created");

        Project saved = new Project();
        saved.setId(1L);
        saved.setName("Created");

        when(projectRepository.save(toCreate)).thenReturn(saved);

        Project result = projectService.createProject(toCreate);

        assertNotNull(result);
        assertEquals("Created", result.getName());
        verify(projectRepository).save(toCreate);
    }

    @Test
    @DisplayName("Should update project when it exists")
    void testUpdateProject_WhenExists() {
        Long id = 1L;

        Project existing = new Project();
        existing.setId(id);
        existing.setName("Old Name");

        Project updates = new Project();
        updates.setName("New Name");

        when(projectRepository.findById(id)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(id, updates);

        assertEquals("New Name", result.getName());
        verify(projectRepository).findById(id);
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent project")
    void testUpdateProject_WhenNotFound() {
        Long id = 404L;
        Project details = new Project();
        details.setName("Anything");

        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            projectService.updateProject(id, details);
        });

        assertEquals("Project not found", ex.getMessage());
        verify(projectRepository).findById(id);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProject_WhenExists() {
        Long id = 1L;
        when(projectRepository.findById(id)).thenReturn(Optional.of(new Project()));

        projectService.deleteProject(id);

        verify(projectRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent project")
    void testDeleteProject_WhenNotFound() {
        Long id = 404L;
        when(projectRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            projectService.deleteProject(id);
        });

        assertEquals("Project not found", ex.getMessage());
        verify(projectRepository, never()).deleteById(any());
    }
}
