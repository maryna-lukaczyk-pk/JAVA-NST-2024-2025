package org.jerzy.projectmanagerapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.jerzy.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;


@Tag(name = "Project", description = "Project managment methods")
@RestController
@RequestMapping("/project")
public class ProjectController {
  private final ProjectService service;

  public ProjectController(ProjectRepository repository) {
    this.service = new ProjectService(repository);
  }

  @Operation(summary = "List all projects")
  @GetMapping
  public List<Project> get() {
    return service.getAllProjects();
  }

  @Operation(summary = "Get a specific project by id")
  @GetMapping("/{id}")
  public Project getProjectById(@Parameter(description = "Project id") String id) {
      return this.service.getById(id);
  }
  
  @Operation(summary = "Create new project")
  @PostMapping("/create")
  public ResponseEntity<Project> post(@Parameter(description = "Project id") Project project) {
    return new ResponseEntity<>(service.create(project), HttpStatus.CREATED);
  }

  @Operation(summary = "Update existing project")
  @PutMapping("/{id}")
  public ResponseEntity<Project> updateProject(@Parameter(description = "Project id") String id, @RequestBody Project project) {
    try {
      Project updatedProject = this.service.update(id, project);
      return ResponseEntity.ok(updatedProject);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Delete project by id")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProject(@Parameter(description = "Project id") String id) {
    try {
      this.service.delete(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}