package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Create and save a new project")
    public Project createProject(@Parameter(description = "Project entity to be created") @RequestBody Project project) {
        return projectService.createProject(project);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Update project by ID")
    public Project updateProject(
            @Parameter(description = "ID of project to update") @PathVariable Long id,
            @Parameter(description = "Updated project data") @RequestBody Project project
    ) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Delete project by ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of project to delete") @PathVariable Long id
    ) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a project", description = "Retrieve a project by ID")
    public Project getProjectById(
            @Parameter(description = "ID of project to retrieve") @PathVariable Long id
    ) {
        return projectService.getProjectById(id);
    }
}
