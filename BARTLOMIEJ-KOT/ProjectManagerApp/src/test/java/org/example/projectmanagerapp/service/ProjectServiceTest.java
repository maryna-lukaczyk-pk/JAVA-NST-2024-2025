package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));
        assertEquals(2, projectService.getAllProjects().size());
    }

    @Test
    void getProjectById() {
        Project p = new Project(); p.setName("Test");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));
        assertEquals("Test", projectService.getProjectById(1L).getName());
    }

    @Test
    void createProject() {
        Project p = new Project(); p.setName("new");
        when(projectRepository.save(p)).thenReturn(p);
        assertEquals("new", projectService.createProject(p).getName());
    }

    @Test
    void updateProject() {
        Project existing = new Project(); existing.setName("old");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        Project input = new Project(); input.setName("new");
        when(projectRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals("new", projectService.updateProject(1L, input).getName());
    }

    @Test
    void deleteProject() {
        projectService.deleteProject(123L);
        verify(projectRepository).deleteById(123L);
    }
}
