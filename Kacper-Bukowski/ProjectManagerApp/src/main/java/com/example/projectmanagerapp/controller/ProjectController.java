package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects")
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
    public ResponseEntity<Project> createProject(
            @Parameter(description = "Project object to be created") @RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project by ID")
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @Parameter(description = "Updated project object") @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(id, projectDetails);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project by ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Project ID") @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}
