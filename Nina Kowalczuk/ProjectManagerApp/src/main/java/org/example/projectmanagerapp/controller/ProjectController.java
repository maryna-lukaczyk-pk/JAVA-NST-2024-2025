package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations related to projects management")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects from the database.")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Adds a new project to the database.")
    public Project createProject(@Parameter(description = "Project object to be created") @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Returns a project by its ID.")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates an existing project by its ID.")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes a project by its ID.")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}