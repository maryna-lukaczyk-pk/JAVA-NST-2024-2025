package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.dto.ProjectDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Kontroler zarządzający operacjami CRUD na projekcie
@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Project operations")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) { this.projectService = projectService; }

    @GetMapping
    @Operation(summary = "Download all projects", description = "Returns a list of all projects")
    public List<ProjectDTO> getAllProjects() { return projectService.getAllProjects(); }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID")
    public ProjectDTO getProjectById(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id) { return projectService.getProjectById(id); }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Adds a new project to the system")
    public ProjectDTO createProject(@Valid @RequestBody Project project) { return projectService.createProject(project); }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates the project data with the given ID")
    public ProjectDTO updateProject(
            @Parameter(description = "Project ID to update", example = "1")
            @PathVariable Long id, @RequestBody Project project) { return projectService.updateProject(id, project); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes the project with the given ID")
    public void deleteProject(
            @Parameter(description = "Project ID to be deleted", example = "1")
            @PathVariable Long id) { projectService.deleteProject(id); }
}