package com.example;


import com.example.entity.Project;
import com.example.repository.ProjectRepository;
import com.example.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectServiceTest {

    private ProjectService projectService;
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    @DisplayName("Should return all projects")
    public void testGetAllProjects() {

        Project project1 = new Project();
        project1.setName("Project 1");
        project1.setDescription("Description 1");

        Project project2 = new Project();
        project2.setName("Project 2");
        project2.setDescription("Description 2");

        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project2));

        List<Project> projects = projectService.getAllProjects();

        assertEquals(2, projects.size());
        assertEquals("Project 1", projects.get(0).getName());
        assertEquals("Project 2", projects.get(1).getName());

        verify(projectRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return project by ID")
    public void testFindById() {
        Project project = new Project();
        project.setId(1L);
        project.setName("TestProject");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestProject", result.get().getName());

        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should save new project")
    public void testSaveProject() {
        Project project = new Project();
        project.setName("NewProject");

        when(projectRepository.save(project)).thenReturn(project);

        Project savedProject = projectService.save(project);

        assertEquals("NewProject", savedProject.getName());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should update existing project")
    public void testUpdateProject() {
        Project project = new Project();
        project.setId(1L);
        project.setName("OldName");
        project.setDescription("OldDesc");

        Project updatedProject = new Project();
        updatedProject.setName("NewName");
        updatedProject.setDescription("NewDesc");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project result = projectService.update(1L, updatedProject);

        assertEquals("NewName", result.getName());
        assertEquals("NewDesc", result.getDescription());

        verify(projectRepository, times(1)).findById(1L);
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    @DisplayName("Should delete project by ID")
    public void testDeleteProject() {
        Long projectId = 1L;

        doNothing().when(projectRepository).deleteById(projectId);

        projectService.delete(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing project")
    public void testUpdateProject_NotFound() {
        Project updatedProject = new Project();
        updatedProject.setName("Name");
        updatedProject.setDescription("Desc");

        when(projectRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            projectService.update(999L, updatedProject);
        });

        assertTrue(thrown.getMessage().contains("Project not found"));
        verify(projectRepository, times(1)).findById(999L);
    }




}
