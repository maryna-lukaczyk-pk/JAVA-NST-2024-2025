package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations related to projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a project by its ID")
    public Project getProjectById(
            @Parameter(description = "ID of the project to be retrieved", required = true)
            @PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing project", description = "Updates an existing project by its ID")
    public Project updateProject(
            @Parameter(description = "ID of the project to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated project data", required = true)
            @RequestBody Project updatedProject) {
        return projectService.updateProject(id, updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project by ID", description = "Deletes the project with the specified ID")
    public void deleteProject(
            @Parameter(description = "ID of the project to be deleted", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project in the database")
    public Project createProject(
            @Parameter(description = "Project to be created", required = true)
            @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PatchMapping("/{projectId}/users/{userId}")
    public Project assignUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        return projectService.assignUserToProject(projectId, userId);
    }
}