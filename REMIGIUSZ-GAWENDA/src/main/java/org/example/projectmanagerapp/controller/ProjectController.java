package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@Tag(name = "Project API", description = "Project management operations")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Operation(summary = "Assign user to project")
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> assignUserToProject(
            @PathVariable Long projectId,
            @RequestParam Long userId
    ) {
        projectService.assignUserToProject(projectId, userId);
        return ResponseEntity.ok().build();
    }


    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Get all projects")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create a new project")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @Operation(summary = "Update project by ID")
    @PutMapping("/{id}")
    public Project updateProject(
        @Parameter(description = "ID of the project to update") @PathVariable Long id,
        @RequestBody Project project
    ) {
        return projectService.updateProject(id, project);
    }

    @Operation(summary = "Delete project by ID")
    @DeleteMapping("/{id}")
    public void deleteProject(
        @Parameter(description = "ID of the project to delete") @PathVariable Long id
    ) {
        projectService.deleteProject(id);
    }
    
    @Operation(summary = "Get project by ID")
    @GetMapping("/{id}")
    public Project getProjectById(
    @Parameter(description = "ID of the project to retrieve") @PathVariable Long id
    ) {
        return projectService.getProjectById(id);
    }

}
