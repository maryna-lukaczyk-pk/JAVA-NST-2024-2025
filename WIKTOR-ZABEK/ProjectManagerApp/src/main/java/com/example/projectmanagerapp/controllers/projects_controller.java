package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.service.projects_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Projects Controller")
@RestController
@RequestMapping("/api/projects")
public class projects_controller {

    @Autowired
    private projects_service projects_service;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns all projects for users")
    public List<projects> getAllProjects() {
        return projects_service.getAllProjects();
    }

    @PostMapping("/")
    @Operation(summary = "Create a project", description = "Saving a project")
    public projects createProject(@RequestBody projects project) {
        return projects_service.create_project(project);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find project by ID", description = "Returns a single project by ID")
    public ResponseEntity<projects> getProjectById(
            @Parameter(description = "ID of the project to retrieve") @PathVariable Long id) {
        Optional<projects> project = projects_service.getProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Updates project information by ID")
    public ResponseEntity<projects> updateProject(
            @Parameter(description = "ID of the project to update") @PathVariable Long id,
            @RequestBody projects project) {
        projects updatedProject = projects_service.updateProject(id, project);
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to delete") @PathVariable Long id) {
        projects_service.deleteProjectById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}