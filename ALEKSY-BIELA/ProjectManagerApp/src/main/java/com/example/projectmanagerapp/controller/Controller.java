package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repozytorium.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "API for managing projects.")
public class Controller {

    private final ProjectRepository projectRepository;

    @Autowired
    public Controller(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Get all projects
    @GetMapping
    @Operation(
            summary = "Find all projects",
            description = "Returns a list of all projects in the database."
    )
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Get project by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Find project by ID",
            description = "Returns a single project matching the provided ID."
    )
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID of the project to be retrieved") @PathVariable Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new project
    @PostMapping
    @Operation(
            summary = "Create a new project",
            description = "Creates a new project and returns the created entity."
    )
    public ResponseEntity<Project> createProject(
            @Parameter(description = "Details of the project to be created") @RequestBody Project project) {
        Project createdProject = projectRepository.save(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    // Update an existing project
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing project",
            description = "Updates the project matching the provided ID with new data."
    )
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of the project to be updated") @PathVariable Long id,
            @Parameter(description = "Updated project data") @RequestBody Project projectDetails) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(projectDetails.getName());
                    Project updatedProject = projectRepository.save(project);
                    return ResponseEntity.ok(updatedProject);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a project
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a project",
            description = "Deletes the project matching the provided ID."
    )
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to be deleted") @PathVariable Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
