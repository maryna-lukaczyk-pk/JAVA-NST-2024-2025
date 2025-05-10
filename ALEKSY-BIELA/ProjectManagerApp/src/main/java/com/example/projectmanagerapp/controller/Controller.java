package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repozytorium.ProjectRepository;
import com.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "API for managing projects.")
public class Controller {

    private final ProjectService projectService;

    @Autowired
    public Controller(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(
            summary = "Find all projects",
            description = "Returns a list of all projects in the database."
    )
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Find project by ID",
            description = "Returns a single project matching the provided ID."
    )
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID of the project to be retrieved") @PathVariable Long id) {
        try{
            Project project = projectService.getProjectById(id);
            return ResponseEntity.ok(project);
        }catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping
    @Operation(
            summary = "Create a new project",
            description = "Creates a new project and returns the created entity."
    )
    public ResponseEntity<Project> createProject(
            @Parameter(description = "Details of the project to be created") @RequestBody Project project) {
        return new ResponseEntity<>(projectService.createProject(project), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing project",
            description = "Updates the project matching the provided ID with new data."
    )
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of the project to be updated") @PathVariable Long id,
            @Parameter(description = "Updated project data") @RequestBody Project projectDetails) {
        return new ResponseEntity<>(projectService.updateProject(id, projectDetails), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a project",
            description = "Deletes the project matching the provided ID."
    )
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to be deleted") @PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

