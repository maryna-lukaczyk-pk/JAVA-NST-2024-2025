package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/project")
@Tag(name = "Projects", description = "API for managing projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves a list of all projects in the system")
    public List<Project> getAllProject() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieves a project by its ID")
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID of the project to retrieve") 
            @PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project with the provided details")
    public Project createProject(
            @Parameter(description = "Project object with details to be created") 
            @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Updates an existing project with the provided details")
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of the project to update") 
            @PathVariable Long id,
            @Parameter(description = "Updated project details") 
            @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(id, projectDetails);
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by its ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to delete") 
            @PathVariable Long id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
