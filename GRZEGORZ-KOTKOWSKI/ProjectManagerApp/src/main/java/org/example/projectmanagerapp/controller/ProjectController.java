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

    @GetMapping
    @Operation(summary = "Get all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create a new project")
    public Project createProject(
            @Parameter(description = "Project to be created", required = true)
            @RequestBody Project project) {
        return projectService.createProject(project);
    }

    // ========================
// Metoda PUT (aktualizacja)
// ========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing project",
            description = "Updates an existing project by its ID"
    )
    public Project updateProject(
            @Parameter(description = "ID of the project to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated project data", required = true)
            @RequestBody Project updatedProject) {
        return projectService.updateProject(id, updatedProject);
    }

    // ==========================
// Metoda DELETE (usuwanie)
// ==========================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a project by ID",
            description = "Deletes the project with the specified ID"
    )
    public void deleteProject(
            @Parameter(description = "ID of the project to be deleted", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
    }
}