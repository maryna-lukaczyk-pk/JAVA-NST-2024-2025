package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Project", description="Operations for managing projects")
class ProjectController {

    private final ProjectRepository repository;

    ProjectController(ProjectRepository repository) {
        this.repository= repository;
    }

    @GetMapping("/projects")
    @Operation(summary="List all projects", description="Returns the list of all projects")
    List<Project> all() {
        return repository.findAll();
    }

    @PostMapping("/projects")
    @Operation(summary="Add a new project", description="Creates a new project")
    Project newProject(@RequestBody Project newProject) {
        return repository.save(newProject);
    }
}