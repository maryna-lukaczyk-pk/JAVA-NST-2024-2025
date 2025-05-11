package org.jerzy.projectmanagerapp.controller;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.jerzy.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectControllerTest {

  private ProjectRepository projectRepository;
  private ProjectService projectService;
  private ProjectController projectController;

  @BeforeEach
  void setUp() {
    projectRepository = Mockito.mock(ProjectRepository.class);
    projectService = new ProjectService(projectRepository);
    projectController = new ProjectController(projectRepository);
  }

  @Test
  void testGetAllProjects() {
    when(projectService.getAllProjects()).thenReturn(List.of(new Project(), new Project()));

    List<Project> result = projectController.get();
    assertEquals(2, result.size());
    verify(projectService).getAllProjects();
  }

  @Test
  void testGetProjectById() {
    Project project = new Project();
    project.setId(1L);
    when(projectService.getById("1")).thenReturn(project);

    Project result = projectController.getProjectById("1");
    assertEquals(1L, result.getId());
  }

  @Test
  void testPost() {
    Project project = new Project();
    project.setName("Test");
    when(projectService.create(any())).thenReturn(project);

    ResponseEntity<Project> response = projectController.post(project);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("Test", response.getBody().getName());
  }

  @Test
  void testUpdateProject_valid() {
    Project project = new Project();
    project.setName("Updated");
    when(projectService.update("1", project)).thenReturn(project);

    ResponseEntity<Project> response = projectController.updateProject("1", project);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Updated", response.getBody().getName());
  }

  @Test
  void testUpdateProject_invalid() {
    Project project = new Project();
    when(projectService.update(eq("1"), any())).thenThrow(new IllegalArgumentException());

    ResponseEntity<Project> response = projectController.updateProject("1", project);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testDeleteProject_valid() {
    ResponseEntity<Void> response = projectController.deleteProject("1");
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(projectService).delete("1");
  }

  @Test
  void testDeleteProject_invalid() {
    doThrow(new IllegalArgumentException()).when(projectService).delete("1");

    ResponseEntity<Void> response = projectController.deleteProject("1");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
