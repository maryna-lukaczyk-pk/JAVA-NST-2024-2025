package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectService.getProjects();
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new project", description = "Creates a new project in the database")
    public ResponseEntity<Project> create(
            @Parameter(description = "Project object to create", required = true)
            @RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Updates an existing project by ID")
    public ResponseEntity<Project> update(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated project object", required = true)
            @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a project by ID", description = "Fetches a single project by its ID")
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID of the project to retrieve", required = true)
            @PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by ID")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> assignUserToProject(@PathVariable Long projectId, @RequestParam Long userId) {
        projectService.assignUserToProject(projectId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<User>> getUsersAssignedToProject(@PathVariable Long projectId) {
        List<User> users = projectService.getUsersAssignedToProject(projectId);
        return ResponseEntity.ok(users);
    }

}