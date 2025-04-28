package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.service.projects_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    @Operation(summary = "Create a project", description = "Saving a project")
    public projects createProject(
            @Parameter(description = "Project object that needs to be saved", required = true)
            @RequestBody projects project) {
        return projects_service.create_project(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a project", description = "Updates a project by its ID")
    public projects updateProject(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated project object", required = true)
            @RequestBody projects project) {
        return projects_service.update_project(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a project", description = "Deletes a project by its ID")
    public void deleteProject(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Long id) {
        projects_service.delete_project(id);
    }
}