package org.jerzy.projectmanagerapp.service;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

  private ProjectRepository projectRepository;
  private ProjectService projectService;

  @BeforeEach
  void setUp() {
    projectRepository = Mockito.mock(ProjectRepository.class);
    projectService = new ProjectService(projectRepository);
  }

  @Test
  void testGetAllProjects() {
    List<Project> projects = List.of(new Project(), new Project());
    when(projectRepository.findAll()).thenReturn(projects);

    List<Project> result = projectService.getAllProjects();
    assertEquals(2, result.size());
    verify(projectRepository).findAll();
  }

  @Test
  void testGetById_validId() {
    Project project = new Project();
    project.setId(1L);
    when(projectRepository.findById(1)).thenReturn(Optional.of(project));

    Project result = projectService.getById("1");
    assertEquals(1L, result.getId());
  }

  @Test
  void testGetById_invalidIdFormat() {
    assertThrows(IllegalArgumentException.class, () -> projectService.getById("abc"));
  }

  @Test
  void testCreateProject() {
    Project project = new Project();
    when(projectRepository.save(project)).thenReturn(project);

    Project result = projectService.create(project);
    assertEquals(project, result);
  }

  @Test
  void testUpdate_validId() {
    Project existing = new Project();
    existing.setId(1L);
    existing.setName("Old Name");

    Project updated = new Project();
    updated.setName("New Name");

    when(projectRepository.findById(1)).thenReturn(Optional.of(existing));
    when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Project result = projectService.update("1", updated);
    assertEquals("New Name", result.getName());
  }

  @Test
  void testUpdate_invalidIdFormat() {
    assertThrows(IllegalArgumentException.class, () -> projectService.update("xyz", new Project()));
  }

  @Test
  void testDelete_validId() {
    when(projectRepository.existsById(1)).thenReturn(true);

    projectService.delete("1");

    verify(projectRepository).deleteById(1);
  }

  @Test
  void testDelete_nonExistingId() {
    when(projectRepository.existsById(99)).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> projectService.delete("99"));
  }

  @Test
  void testDelete_invalidIdFormat() {
    assertThrows(IllegalArgumentException.class, () -> projectService.delete("bad_id"));
  }
}
