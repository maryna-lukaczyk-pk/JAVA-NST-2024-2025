package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.service.ProjectService;
import org.example.projectmanagerapp.entity.Project;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Project operations")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Download all projects", description = "Returns a list of all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Adds a new project to the system")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Updates the project data with the given ID")
    public Project updateProject(
            @Parameter(description = "Project ID to update", example = "1")
            @PathVariable Long id, @RequestBody Project project) { return projectService.updateProject(id, project); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Deletes the project with the given ID")
    public void deleteProject(
            @Parameter(description = "Project ID to be deleted", example = "1")
            @PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
