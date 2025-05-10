package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(method = "POST", summary = "Add new projects")
    @PostMapping("/projects")
    public ResponseEntity<Project> newProject(@RequestBody Project newProject) {
        Project createdProject = projectService.createProject(newProject);
        return new ResponseEntity<> (createdProject, HttpStatus.CREATED);
    }
}