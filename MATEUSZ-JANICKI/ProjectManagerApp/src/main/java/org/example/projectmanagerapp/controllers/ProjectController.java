package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Project")
@RestController
public class ProjectController {
    private final ProjectService projectService;

    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "API Project POST method")
    @GetMapping("/projects")
    List<Project> all() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "API Project POST method")
    @PostMapping("/projects")
    public ResponseEntity<Project> newProject(@RequestBody Project newProject) {
        Project createdProject = projectService.createProject(newProject);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
}