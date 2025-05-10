package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.service.ProjectService;
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

public class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("getAllProjects() should return all projects")
    void testGetAllProjects() {
        Project p1 = new Project(); p1.setId(1L); p1.setName("P1");
        Project p2 = new Project(); p2.setId(2L); p2.setName("P2");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getProject() should return project when found")
    void testGetProjectFound() {
        Project p = new Project(); p.setId(10L); p.setName("Found");
        when(projectRepository.findById(10L)).thenReturn(Optional.of(p));

        Project result = projectService.getProject(10L);

        assertNotNull(result);
        assertEquals("Found", result.getName());
        verify(projectRepository, times(1)).findById(10L);
    }

    @Test
    @DisplayName("getProject() should return null when not found")
    void testGetProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        Project result = projectService.getProject(99L);

        assertNull(result);
        verify(projectRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("createProject() should save and return project")
    void testCreateProject() {
        Project input = new Project(); input.setName("NewProj");
        Project saved = new Project(); saved.setId(5L); saved.setName("NewProj");
        when(projectRepository.save(input)).thenReturn(saved);

        Project result = projectService.createProject(input);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("NewProj", result.getName());
        verify(projectRepository, times(1)).save(input);
    }

    @Test
    @DisplayName("updateProject() should update existing project")
    void testUpdateProjectFound() {
        Project existing = new Project(); existing.setId(20L); existing.setName("Old");
        Project updateData = new Project(); updateData.setId(20L); updateData.setName("Updated");
        when(projectRepository.findById(20L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.updateProject(updateData);

        assertNotNull(result);
        assertEquals(20L, result.getId());
        assertEquals("Updated", result.getName());
        verify(projectRepository).findById(20L);
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("updateProject() should return null when project not found")
    void testUpdateProjectNotFound() {
        Project updateData = new Project(); updateData.setId(99L); updateData.setName("X");
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());

        Project result = projectService.updateProject(updateData);

        assertNull(result);
        verify(projectRepository).findById(99L);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteProject() should delete and return existing project")
    void testDeleteProjectFound() {
        Project existing = new Project(); existing.setId(30L); existing.setName("ToDelete");
        when(projectRepository.findById(30L)).thenReturn(Optional.of(existing));

        Project result = projectService.deleteProject(30L);

        assertNotNull(result);
        assertEquals(30L, result.getId());
        verify(projectRepository).findById(30L);
        verify(projectRepository).delete(existing);
    }

    @Test
    @DisplayName("deleteProject() should return null when project not found")
    void testDeleteProjectNotFound() {
        when(projectRepository.findById(123L)).thenReturn(Optional.empty());

        Project result = projectService.deleteProject(123L);

        assertNull(result);
        verify(projectRepository).findById(123L);
        verify(projectRepository, never()).delete(any());
    }
}
