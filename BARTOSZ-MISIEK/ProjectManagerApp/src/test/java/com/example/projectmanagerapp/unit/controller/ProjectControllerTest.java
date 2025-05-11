package com.example.projectmanagerapp.unit.controller;

import org.example.projectmanager.controller.ProjectController;
import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.ProjectRepository;
import org.example.projectmanager.service.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class ProjectControllerTest {
    private ProjectRepository projectRepository;
    private ProjectController projectController;

    @BeforeEach
    public void setUp() {
        projectRepository = Mockito.mock(ProjectRepository.class);
        ProjectService projectService = new ProjectService(projectRepository);
        projectController = new ProjectController(projectService);
    }

    @Test
    public void Get_ShouldReturn_Project() throws EntityNotFoundException {
        var projectId = 1L;
        var project = new Project();
        project.setId(projectId);
        project.setName("Project A");

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        var result = projectController.get(projectId);

        Assertions.assertEquals(project.getId(), result.id);
        Assertions.assertEquals(project.getName(), result.name);
    }

    @Test
    public void Get_ShouldThrow_EntityNotFoundException() {
        var projectId = 1L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> projectController.get(projectId));
    }

    @Test
    public void Create_ShouldReturn_CreatedProjectId() {
        var project = new Project();
        project.setId(1L);
        project.setName("Project A");

        Mockito.when(projectRepository.save(Mockito.any(Project.class))).thenReturn(project);

        var dto = new org.example.projectmanager.dto.project.ProjectCreateDto();
        dto.name = "Project A";

        var result = projectController.create(dto);

        Assertions.assertEquals(project.getId(), result);
    }

    @Test
    public void Delete_ShouldDelete_Project() throws EntityNotFoundException {
        var projectId = 1L;

        Mockito.when(projectRepository.existsById(projectId)).thenReturn(true);

        projectController.delete(projectId);

        Mockito.verify(projectRepository).deleteById(projectId);
    }

    @Test
    public void Delete_ShouldThrow_EntityNotFoundException() {
        var projectId = 1L;

        Mockito.when(projectRepository.existsById(projectId)).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> projectController.delete(projectId));
    }

    @Test
    public void Update_ShouldUpdate_Project() throws EntityNotFoundException {
        var projectId = 1L;
        var project = new Project();
        project.setId(projectId);
        project.setName("Project A");

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        var dto = new org.example.projectmanager.dto.project.ProjectEditDto();
        dto.id = projectId;
        dto.name = "Project B";

        projectController.update(dto);

        Assertions.assertEquals("Project B", project.getName());
    }

    @Test
    public void Update_ShouldThrow_EntityNotFoundException() {
        var projectId = 1L;

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        var dto = new org.example.projectmanager.dto.project.ProjectEditDto();
        dto.id = projectId;

        Assertions.assertThrows(EntityNotFoundException.class, () -> projectController.update(dto));
    }
}