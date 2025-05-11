package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Projects")
class ProjectController {

    private final ProjectService projectService;

    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(method = "GET", summary = "Get an array of all projects")
    @GetMapping("/projects")
    List<Project> all() {
        return projectService.getAllProjects();
    }

    @Operation(method = "GET", summary = "Get a project by its ID")
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID of the project to retrieve") @PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(method = "POST", summary = "Add new projects")
    @PostMapping("/projects")
    public ResponseEntity<Project> newProject(@RequestBody Project newProject) {
        Project createdProject = projectService.createProject(newProject);
        return new ResponseEntity<> (createdProject, HttpStatus.CREATED);
    }
    
    @Operation(method = "PUT", summary = "Update an existing project by its ID")
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of the project to update") @PathVariable Long id,
            @RequestBody Project updatedProject) {
        return projectService.updateProject(id, updatedProject)
                .map(project -> new ResponseEntity<>(project, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(method = "DELETE", summary = "Delete an existing project by its ID")
    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to delete") @PathVariable Long id) {
        if (projectService.deleteProject(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}