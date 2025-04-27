package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Projects")
class ProjectController {

    private final ProjectRepository repository;

    ProjectController(ProjectRepository repository) {
        this.repository= repository;
    }

    @Operation(method = "GET", summary = "Get an array of all projects")
    @GetMapping("/projects")
    List<Project> all() {
        return repository.findAll();
    }

    @Operation(method = "POST", summary = "Add new projects")
    @PostMapping("/projects")
    Project newProject(@RequestBody Project newProject) {
        return repository.save(newProject);
    }
}