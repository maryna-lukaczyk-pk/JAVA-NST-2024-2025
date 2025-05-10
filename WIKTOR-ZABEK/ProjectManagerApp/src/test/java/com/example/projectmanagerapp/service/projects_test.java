package com.example.projectmanagerapp.service;


import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.repository.projects_repository;
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
public class projects_test {

    @Mock
    private projects_repository projects_repository;

    @InjectMocks
    private projects_service projects_service;

    private projects project1;
    private projects project2;

    @BeforeEach
    void setUp() {
        project1 = new projects();
        project1.setId(1L);
        project1.setName("Project 1");

        project2 = new projects();
        project2.setId(2L);
        project2.setName("Project 2");
    }

    @Test
    @DisplayName("Should return all projects")
    void testGetAllProjects() {
        when(projects_repository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<projects> result = projects_service.getAllProjects();

        assertEquals(2, result.size());
        verify(projects_repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a project")
    void testCreateProject() {
        when(projects_repository.save(any(projects.class))).thenReturn(project1);

        projects result = projects_service.create_project(project1);

        assertNotNull(result);
        assertEquals(project1.getName(), result.getName());
        verify(projects_repository, times(1)).save(any(projects.class));
    }

    @Test
    @DisplayName("Should find project by ID")
    void testGetProjectById() {
        when(projects_repository.findById(1L)).thenReturn(Optional.of(project1));

        Optional<projects> result = projects_service.getProjectById(1L);

        assertTrue(result.isPresent());
        assertEquals(project1.getName(), result.get().getName());
        verify(projects_repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when project not found")
    void testGetProjectByIdNotFound() {
        when(projects_repository.findById(3L)).thenReturn(Optional.empty());

        Optional<projects> result = projects_service.getProjectById(3L);

        assertFalse(result.isPresent());
        verify(projects_repository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Should delete project by ID")
    void testDeleteProjectById() {
        Long projectId = 1L;
        doNothing().when(projects_repository).deleteById(projectId);

        projects_service.deleteProjectById(projectId);

        verify(projects_repository, times(1)).deleteById(projectId);
    }

    @Test
    @DisplayName("Should update project")
    void testUpdateProject() {
        Long projectId = 1L;
        projects updatedProject = new projects();
        updatedProject.setName("Updated Project");

        when(projects_repository.findById(projectId)).thenReturn(Optional.of(project1));
        when(projects_repository.save(any(projects.class))).thenAnswer(invocation -> invocation.getArgument(0));

        projects result = projects_service.updateProject(projectId, updatedProject);

        assertNotNull(result);
        assertEquals("Updated Project", result.getName());
        verify(projects_repository, times(1)).findById(projectId);
        verify(projects_repository, times(1)).save(any(projects.class));
    }


}