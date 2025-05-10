package com.example.projectmanagerapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import java.util.List;

@Tag(name = "Project Controller", description = "Operations on projects - retrieving, creating, updating, deleting information")
@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAll();
    }

    @Operation(summary = "Get project by ID", description = "Retrieve a single project by its ID")
    @GetMapping("/{id}")
    public Project getProjectById(@Parameter(description = "ID of the project") @PathVariable int id) {
        return projectService.getById(id);
    }

    @Operation(summary = "Create new project", description = "Create a new project with given data")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.create(project);
    }

    @Operation(summary = "Update project", description = "Update an existing project with given ID")
    @PutMapping("/{id}")
    public Project updateProject(
            @Parameter(description = "ID of the project to update") @PathVariable int id,
            @RequestBody Project project) {
        return projectService.update(id, project);
    }

    @Operation(summary = "Delete project", description = "Delete an existing project by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@Parameter(description = "ID of the project to delete") @PathVariable int id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
