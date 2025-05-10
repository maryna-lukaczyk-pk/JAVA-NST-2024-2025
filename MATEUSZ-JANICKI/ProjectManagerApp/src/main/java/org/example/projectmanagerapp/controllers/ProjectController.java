package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project", description = "Operations for mapping projects")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Retrieve all projects", description = "Return a list of all projects from database")
    @GetMapping("/all")
    List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create new project", description = "Adds a new project to database")
    @PostMapping("/create")
    public ResponseEntity<Project> addProject(@RequestBody Project newProject) {
        Project createdProject = projectService.createProject(newProject);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "Update project by ID", description = "Updating project by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@Parameter(description = "ID of the project to update") @PathVariable Long id, @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @Operation(summary = "Delete project by ID", description = "Deleting project by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@Parameter(description = "ID of the project to delete") @PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
    }
}