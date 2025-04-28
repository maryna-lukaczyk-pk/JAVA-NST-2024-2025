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
        try {
            var field = ProjectService.class.getDeclaredField("projectRepository");
            field.setAccessible(true);
            field.set(projectService, projectRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Powinien zwrócić wszystkie projekty")
    void testGetAllProjects() {
        Project proj1 = new Project("Proj1", "Desc1");
        Project proj2 = new Project("Proj2", "Desc2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(proj1, proj2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Powinien zwrócić projekt po ID, gdy istnieje")
    void testGetProjectByIdFound() {
        Project project = new Project("Proj", "Desc");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals("Proj", result.get().getName());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Powinien zwrócić pusty Optional, gdy projekt nie istnieje")
    void testGetProjectByIdNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Project> result = projectService.getProjectById(99L);

        assertFalse(result.isPresent());
        verify(projectRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Powinien stworzyć nowy projekt")
    void testCreateProject() {
        Project project = new Project("NewProject", "Desc");
        when(projectRepository.save(project)).thenReturn(project);

        Project created = projectService.createProject(project);

        assertEquals("NewProject", created.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Powinien zaktualizować istniejący projekt")
    void testUpdateProjectFound() {
        Project existing = new Project("OldName", "OldDesc");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project update = new Project("NewName", "NewDesc");

        Project updated = projectService.updateProject(1L, update);

        assertNotNull(updated);
        assertEquals("NewName", updated.getName());
        assertEquals("NewDesc", updated.getDescription());
        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Powinien zwrócić null przy próbie aktualizacji nieistniejącego projektu")
    void testUpdateProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        Project update = new Project("Name", "Desc");

        Project updated = projectService.updateProject(99L, update);

        assertNull(updated);
        verify(projectRepository, times(1)).findById(99L);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("Powinien usunąć projekt po ID")
    void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).deleteById(1L);
    }
}