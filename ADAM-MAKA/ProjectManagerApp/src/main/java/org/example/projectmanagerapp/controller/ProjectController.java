package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.example.projectmanagerapp.dto.AssignUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Operations related to projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all projects", description = "Get a list of all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Integer id) {
        Project project = projectService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new project", description = "Add a new project to the system")
    public Project createProject(
            @Parameter(description = "Project data to create a new project", required = true)
            @RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED).getBody();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project", description = "Update the project with the given ID")
    public Project updateProject(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Updated project data", required = true)
            @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing project", description = "Delete the project with the given ID")
    public void deleteProject(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Integer id) {
        projectService.deleteProject(id);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<Project> assignUserToProject(
            @PathVariable Integer id,
            @RequestBody AssignUserRequest request) {
        Project updated = projectService.assignUser(id, request.getUserId());
        return ResponseEntity.ok(updated);
    }
}