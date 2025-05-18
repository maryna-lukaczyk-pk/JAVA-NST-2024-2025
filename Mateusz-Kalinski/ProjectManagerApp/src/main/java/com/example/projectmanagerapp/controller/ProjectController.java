package com.example.projectmanagerapp.controller;



import com.example.projectmanagerapp.entity.Project;

import com.example.projectmanagerapp.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



import java.util.List;

import java.util.Optional;



@RestController

@RequestMapping("/api/projects")

@Tag(name = "Projects", description = "Endpoints for managing projects")

public class ProjectController {



    private final ProjectService projectService;



    public ProjectController(ProjectService projectService) {

        this.projectService = projectService;

    }



    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects in the system")

    @GetMapping("/all")

    public List<Project> getAllProjects() {

        return projectService.getAllProjects();

    }



    @Operation(summary = "Get project by ID", description = "Retrieve a specific project by its ID")

    @GetMapping("/{id}")

    public ResponseEntity<Project> getProjectById(

            @Parameter(description = "ID of the project to be retrieved", required = true, example = "1")

            @PathVariable Long id) {

        Optional<Project> projectOptional = projectService.getProjectById(id);

        return projectOptional.map(ResponseEntity::ok)

                .orElseGet(() -> ResponseEntity.notFound().build());

    }



    @Operation(summary = "Create new project", description = "Create a new project with provided details")

    @PostMapping("/create")

    public ResponseEntity<Project> createProject(

            @Parameter(description = "Project object to create. Name is required.", required = true)

            @RequestBody Project project) {

        Project createdProject = projectService.createProject(project);

        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);

    }



    @Operation(summary = "Update project", description = "Update an existing project by its ID")

    @PutMapping("/update/{id}")

    public ResponseEntity<Project> updateProject(

            @Parameter(description = "ID of the project to update", required = true, example = "1")

            @PathVariable Long id,

            @Parameter(description = "Project object with updated information. Name is required.", required = true)

            @RequestBody Project projectDetails) {

        try {

            Project updatedProject = projectService.updateProject(id, projectDetails);

            if (updatedProject != null) {

                return ResponseEntity.ok(updatedProject);

            } else {

                return ResponseEntity.notFound().build();

            }

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();

        }

    }



    @Operation(summary = "Delete project", description = "Delete a project by its ID")

    @DeleteMapping("/delete/{id}")

    public ResponseEntity<Void> deleteProject(

            @Parameter(description = "ID of the project to delete", required = true, example = "1")

            @PathVariable Long id) {

        try {

            projectService.deleteProject(id);

            return ResponseEntity.noContent().build();

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();

        }

    }



    @Operation(summary = "Add user to project", description = "Assigns an existing user to an existing project")

    @PostMapping("/{projectId}/users/{userId}") // lub .../users z ID usera w body

    public ResponseEntity<Project> addUserToProject(

            @Parameter(description = "ID of the project", required = true) @PathVariable Long projectId,

            @Parameter(description = "ID of the user to add", required = true) @PathVariable Long userId) {

        try {

            Project updatedProject = projectService.addUserToProject(projectId, userId);

            return ResponseEntity.ok(updatedProject);

        } catch (RuntimeException e) {

// Możesz tu dodać bardziej szczegółową obsługę błędów, np. zwracanie 404

            return ResponseEntity.notFound().build();

        }

    }

}