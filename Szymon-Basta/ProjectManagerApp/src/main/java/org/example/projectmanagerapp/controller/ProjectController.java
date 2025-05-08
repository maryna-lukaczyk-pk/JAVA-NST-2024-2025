package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.service.ProjectService;
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

    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects from the database")
    @GetMapping
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create a new project", description = "Creates a project with the provided information")
    @PostMapping
    public ResponseEntity<Projects> createProject(
            @Parameter(description = "Project object to be created")
            @RequestBody Projects project) {
        Projects createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
}
