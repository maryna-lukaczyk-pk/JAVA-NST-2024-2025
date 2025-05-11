package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@Tag(
        name = "Project Management",
        description = "APIs for managing projects"
)

public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Get all projects",
            description = "Retrieve a list of all projects"
    )

    @GetMapping
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(
            summary = "Create a new project",
            description = "Create a new project with the provided details"
    )

    @PostMapping

    public ResponseEntity <Projects> addProjekt(
            @RequestBody Projects projects) {
        Projects createdProject = projectService.createProject(projects);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }


}