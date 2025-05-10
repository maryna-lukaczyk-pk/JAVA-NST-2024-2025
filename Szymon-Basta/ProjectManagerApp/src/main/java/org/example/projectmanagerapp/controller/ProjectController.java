package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects from the database")
    @GetMapping
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create a new project", description = "Creates a project with the provided information")
    @PostMapping
    public ResponseEntity<Projects> createProject(
            @Parameter(description = "Project object to be created")
            @RequestBody Projects project) {
        Projects createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a project", description = "Updates an existing project by ID")
    @PutMapping("/{id}")
    public Projects updateProject(
            @Parameter(description = "ID of the project to be updated") @PathVariable Long id,
            @RequestBody Projects project) {
        return projectService.updateProject(id, project);
    }

    @Operation(summary = "Delete a project", description = "Deletes a project by ID")
    @DeleteMapping("/{id}")
    public void deleteProject(
            @Parameter(description = "ID of the project to be deleted") @PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @Operation(summary = "Get project by ID", description = "Retrieves a project by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Projects> getProjectById(
            @Parameter(description = "ID of the project to retrieve") @PathVariable Long id) {
        Projects project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

}
