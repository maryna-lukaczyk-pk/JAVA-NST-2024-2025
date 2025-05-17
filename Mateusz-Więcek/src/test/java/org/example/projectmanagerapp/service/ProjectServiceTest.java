package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project p1;
    private Project p2;

    @BeforeEach
    void setUp() {
        p1 = new Project(1L, "Alpha");
        p2 = new Project(2L, "Beta");
    }

    @Test
    @DisplayName("getAllProjects returns all projects")
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        List<Project> all = projectService.getAllProjects();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("getProjectById returns project when exists")
    void getProjectByIdExists() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));
        Optional<Project> opt = projectService.getProjectById(1L);
        assertTrue(opt.isPresent());
        assertEquals("Alpha", opt.get().getName());
    }

    @Test
    @DisplayName("getProjectById returns empty when not exists")
    void getProjectByIdNotExists() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(projectService.getProjectById(99L).isPresent());
    }

    @Test
    @DisplayName("createProject saves the project")
    void createProject() {
        when(projectRepository.save(p1)).thenReturn(p1);
        assertEquals("Alpha", projectService.createProject(p1).getName());
    }

    @Test
    @DisplayName("updateProject updates when exists")
    void updateProjectExists() {
        Project updates = new Project(null, "AlphaX");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(projectRepository.save(any(Project.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Optional<Project> opt = projectService.updateProject(1L, updates);
        assertTrue(opt.isPresent());
        assertEquals("AlphaX", opt.get().getName());
    }

    @Test
    @DisplayName("updateProject returns empty when not exists")
    void updateProjectNotExists() {
        when(projectRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(projectService.updateProject(99L, p1).isPresent());
    }

    @Test
    @DisplayName("deleteProject returns true when it existed")
    void deleteProjectExists() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        assertTrue(projectService.deleteProject(1L));
        verify(projectRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteProject returns false when it did not exist")
    void deleteProjectNotExists() {
        when(projectRepository.existsById(99L)).thenReturn(false);
        assertFalse(projectService.deleteProject(99L));
        verify(projectRepository, never()).deleteById(anyLong());
    }
}
