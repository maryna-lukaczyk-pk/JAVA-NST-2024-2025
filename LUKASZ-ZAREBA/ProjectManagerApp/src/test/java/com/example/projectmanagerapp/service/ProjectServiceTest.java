package com.example.projectmanagerapp.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    @Test
    void getAll_ReturnsAllProjects() {
        Project project1 = new Project();
        Project project2 = new Project();
        List<Project> mockProjects = Arrays.asList(project1, project2);
        when(projectRepository.findAll()).thenReturn(mockProjects);

        List<Project> result = projectService.getAll();

        assertEquals(mockProjects, result);
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void getById_ExistingProject_ReturnsProject() {
        Project project = new Project();
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        Project result = projectService.getById(1);

        assertEquals(project, result);
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void getById_NonExistingProject_ThrowsException() {
        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> projectService.getById(1));
        verify(projectRepository, times(1)).findById(1);
    }

    @Test
    void create_Project_SavesAndReturnsProject() {
        Project newProject = new Project();
        Project savedProject = new Project();
        when(projectRepository.save(newProject)).thenReturn(savedProject);

        Project result = projectService.create(newProject);

        assertEquals(savedProject, result);
        verify(projectRepository, times(1)).save(newProject);
    }

    @Test
    void update_ExistingProject_UpdatesAndReturnsProject() {
        Project updates = new Project();
        Project updatedProject = new Project();
        updatedProject.setId(1);
        when(projectRepository.existsById(1)).thenReturn(true);
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.update(1, updates);

        assertEquals(updatedProject, result);
        assertEquals(1, result.getId());
        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void update_NonExistingProject_ThrowsException() {
        when(projectRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> projectService.update(1, new Project()));
        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void delete_ExistingProject_DeletesProject() {
        when(projectRepository.existsById(1)).thenReturn(true);

        projectService.delete(1);

        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, times(1)).deleteById(1);
    }

    @Test
    void delete_NonExistingProject_ThrowsException() {
        when(projectRepository.existsById(1)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> projectService.delete(1));
        verify(projectRepository, times(1)).existsById(1);
        verify(projectRepository, never()).deleteById(anyInt());
    }
}

