package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Project", description = "Project management")
public class ProjectController {

    // projectRepository field declaration
    // "final" means that a field needs to be initialized
    // using constructor and cannot be changed later
    private final ProjectService projectService;

    // constructor for ProjectController class
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all projects", description = "Returns list of all projects")
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Returns project by ID")
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID of project to be retrieved" ,required = true)
            @PathVariable Long id
    ) {
        return projectService.findById(id)
                             .map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new project", description = "Creates a new project")
    public Project createProject(
            @Parameter(description = "Project to be created" ,required = true)
            @RequestBody Project project
    ) {
        return projectService.create(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project", description = "Updates an existing project")
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of project to be updated" ,required = true)
            @PathVariable Long id,
            @Parameter(description = "New project data" ,required = true)
            @RequestBody Project project
    ) {
        return projectService.findById(id)
                             .map(existing -> ResponseEntity.ok(projectService.update(id, project)))
                             .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing project", description = "Deletes an existing project")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of project to be deleted" ,required = true)
            @PathVariable Long id
    ) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}