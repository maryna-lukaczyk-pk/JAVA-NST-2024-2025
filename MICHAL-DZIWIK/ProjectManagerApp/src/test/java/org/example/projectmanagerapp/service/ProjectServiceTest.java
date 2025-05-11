package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("getAllProjects should return list of all projects")
    void getAllProjects_returnsAll() {
        Project p1 = new Project();
        p1.setId(1L);
        p1.setName("Proj1");
        Project p2 = new Project();
        p2.setId(2L);
        p2.setName("Proj2");
        when(projectRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Project> projects = projectService.getAllProjects();

        assertThat(projects).hasSize(2);
        verify(projectRepository).findAll();
    }

    @Test
    @DisplayName("createProject should save and return project")
    void createProject_savesAndReturns() {
        Project p = new Project();
        p.setName("NewProj");
        Project saved = new Project();
        saved.setId(1L);
        saved.setName("NewProj");
        when(projectRepository.save(p)).thenReturn(saved);

        Project result = projectService.createProject(p);

        assertThat(result).isEqualTo(saved);
        verify(projectRepository).save(p);
    }

    @Test
    @DisplayName("getProjectById when found returns project")
    void getProjectById_found_returnsProject() {
        Project p = new Project();
        p.setId(1L);
        p.setName("Proj");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));

        Project result = projectService.getProjectById(1L);

        assertThat(result).isEqualTo(p);
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("getProjectById when not found returns null")
    void getProjectById_notFound_returnsNull() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Project result = projectService.getProjectById(1L);

        assertThat(result).isNull();
        verify(projectRepository).findById(1L);
    }

    @Test
    @DisplayName("updateProject when exists updates and returns")
    void updateProject_exists_updatesAndReturns() {
        Project existing = new Project();
        existing.setId(1L);
        existing.setName("Old");
        Project details = new Project();
        details.setName("Updated");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(projectRepository.save(existing)).thenReturn(existing);

        Project result = projectService.updateProject(1L, details);

        assertThat(result.getName()).isEqualTo("Updated");
        verify(projectRepository).findById(1L);
        verify(projectRepository).save(existing);
    }

    @Test
    @DisplayName("updateProject when not exists returns null")
    void updateProject_notExists_returnsNull() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Project result = projectService.updateProject(1L, new Project());

        assertThat(result).isNull();
        verify(projectRepository).findById(1L);
        verify(projectRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteProject when exists deletes")
    void deleteProject_exists_deletes() {
        when(projectRepository.existsById(1L)).thenReturn(true);

        projectService.deleteProject(1L);

        verify(projectRepository).existsById(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteProject when not exists does nothing")
    void deleteProject_notExists_noDelete() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        projectService.deleteProject(1L);

        verify(projectRepository).existsById(1L);
        verify(projectRepository, never()).deleteById(anyLong());
    }
}