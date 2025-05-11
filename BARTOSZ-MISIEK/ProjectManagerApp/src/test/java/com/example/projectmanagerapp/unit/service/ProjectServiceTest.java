package com.example.projectmanagerapp.unit.service;

import org.example.projectmanager.dto.project.ProjectCreateDto;
import org.example.projectmanager.dto.project.ProjectDto;
import org.example.projectmanager.dto.project.ProjectEditDto;
import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.ProjectRepository;
import org.example.projectmanager.service.IProjectService;
import org.example.projectmanager.service.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class ProjectServiceTest {
    private ProjectRepository repository;
    private IProjectService service;

    @BeforeEach
    public void setUp() {
        repository = Mockito.mock(ProjectRepository.class);
        service = new ProjectService(repository);
    }

    @Test
    void FindAll_ShouldReturn_AllProjects() {
        var project = new Project();
        project.setId(1L);
        project.setName("Project A");

        var expected = ProjectDto.MapFromEntity(project);
        Mockito.when(repository.findAll()).thenReturn(List.of(project));

        var result = service.findAll();

        Assertions.assertEquals(List.of(expected), result);
    }

    @Test
    void Create_ShouldReturn_CreatedProjectId() {
        var project = new Project();
        project.setId(1L);
        project.setName("Project A");
        Mockito.when(repository.save(Mockito.any(Project.class))).thenReturn(project);

        var dto = new ProjectCreateDto();
        dto.name = "Project A";

        var result = service.create(dto);

        Assertions.assertEquals(project.getId(), result);
    }

    @Test
    void Delete_ShouldDelete_Project() throws EntityNotFoundException {
        var project = new Project();
        project.setId(1L);
        Mockito.when(repository.existsById(project.getId())).thenReturn(true);

        service.delete(project.getId());

        Mockito.verify(repository).deleteById(project.getId());
    }

    @Test
    void Delete_ShouldThrow_EntityNotFoundException() {
        var project = new Project();
        project.setId(1L);
        Mockito.when(repository.existsById(project.getId())).thenReturn(false);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.delete(project.getId()));
    }

    @Test
    void Edit_ShouldUpdate_Project() throws EntityNotFoundException {
        var project = new Project();
        project.setId(1L);
        project.setName("Project A");
        Mockito.when(repository.findById(project.getId())).thenReturn(Optional.of(project));

        var dto = new ProjectEditDto();
        dto.id = 1L;
        dto.name = "Project B";

        service.edit(dto);

        Assertions.assertEquals("Project B", project.getName());
    }

    @Test
    void Edit_ShouldThrow_EntityNotFoundException() {
        var dto = new ProjectEditDto();
        dto.id = 1L;
        Mockito.when(repository.findById(dto.id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.edit(dto));
    }

    @Test
    void GetById_ShouldReturn_Project() throws EntityNotFoundException {
        var project = new Project();
        project.setId(1L);
        project.setName("Project A");
        Mockito.when(repository.findById(project.getId())).thenReturn(Optional.of(project));

        var result = service.getById(project.getId());

        Assertions.assertEquals(ProjectDto.MapFromEntity(project), result);
    }
}
