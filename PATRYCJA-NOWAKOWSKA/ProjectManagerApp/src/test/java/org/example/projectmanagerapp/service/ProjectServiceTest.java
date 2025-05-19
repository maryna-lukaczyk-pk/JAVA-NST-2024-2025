package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

    private final ProjectRepository projectRepository = Mockito.mock(ProjectRepository.class);
    private final ProjectService projectService = new ProjectService(projectRepository);

    @Test
    @DisplayName("Powinien zwrócić wszystkie projekty")
    void testGetAllProjects() {
        Project p1 = new Project();
        p1.setName("Projekt A");

        Project p2 = new Project();
        p2.setName("Projekt B");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> result = projectService.getAllProjects();

        assertEquals(2, result.size());
        verify(projectRepository, times(1)).findAll();
    }
}
