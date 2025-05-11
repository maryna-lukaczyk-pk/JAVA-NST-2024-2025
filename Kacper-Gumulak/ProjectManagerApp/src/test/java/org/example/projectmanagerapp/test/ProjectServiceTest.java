package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.ProjectDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Testy związane z serwisem projektu
class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project p = new Project("Proj1"); p.setId(1L);
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p));

        var list = projectService.getAllProjects();

        assertEquals(1, list.size());
        assertEquals("Proj1", list.get(0).name());
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    void testGetProjectById_Found() {
        Project p = new Project("X"); p.setId(7L);
        when(projectRepository.findById(7L)).thenReturn(Optional.of(p));

        ProjectDTO dto = projectService.getProjectById(7L);

        assertEquals(7L, dto.id());
        assertEquals("X", dto.name());
    }

    @Test
    @DisplayName("getProjectById throws 404 when not found")
    void testGetProjectById_NotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> projectService.getProjectById(99L));
    }

    @Test
    @DisplayName("Should create project")
    void testCreateProject() {
        Project p = new Project("NewP"); p.setId(3L);
        when(projectRepository.save(any(Project.class))).thenReturn(p);

        ProjectDTO dto = projectService.createProject(new Project("ignore"));

        assertEquals(3L, dto.id());
        assertEquals("NewP", dto.name());
    }

    @Test
    @DisplayName("Should delete project or throw if missing")
    void testDeleteProject() {
        when(projectRepository.existsById(4L)).thenReturn(true);
        assertDoesNotThrow(() -> projectService.deleteProject(4L));

        when(projectRepository.existsById(5L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> projectService.deleteProject(5L));
    }
}