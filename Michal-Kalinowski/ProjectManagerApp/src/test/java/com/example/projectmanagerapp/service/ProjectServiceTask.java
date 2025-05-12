// src/test/java/com/example/projectmanagerapp/service/ProjectServiceTest.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService    = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        Project p1 = new Project(); p1.setName("P1");
        Project p2 = new Project(); p2.setName("P2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> projects = projectService.getProjects();

        assertEquals(2, projects.size());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find project by ID")
    void testGetProjectById() {
        Project p = new Project(); p.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));

        Project result = projectService.getProjectById(1L);

        assertEquals(1L, result.getId());
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create project")
    void testCreateProject() {
        Project p = new Project(); p.setName("New");
        when(projectRepository.save(p)).thenReturn(p);

        Project result = projectService.createProject(p);

        assertEquals("New", result.getName());
        verify(projectRepository).save(p);
    }

    @Test
    @DisplayName("Should update existing project")
    void testUpdateProject() {
        Project existing = new Project(); existing.setId(1L);
        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Project toUpdate = new Project(); toUpdate.setName("Upd");
        Project result   = projectService.updateProject(1L, toUpdate);

        assertEquals("Upd", result.getName());
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Should delete project")
    void testDeleteProject() {
        doNothing().when(projectRepository).deleteById(1L);

        projectService.deleteProject(1L);

        verify(projectRepository).deleteById(1L);
    }
    void testGetProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> projectService.getProjectById(99L));
    }

    @Test @DisplayName("updateProject() – not found")
    void testUpdateProjectNotFound() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> projectService.updateProject(99L, new Project()));
    }
}
