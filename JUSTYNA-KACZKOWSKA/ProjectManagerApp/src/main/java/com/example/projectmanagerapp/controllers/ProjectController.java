package com.example.projectmanagerapp.controllers;


import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Tag(name = "Project", description = "Methods of Project")
public class ProjectController {
    private final ProjectRepository repository;

    ProjectController(ProjectRepository repository) {
        this.repository= repository;
    }

    @Operation(summary = "Get all projects")
    @GetMapping("/projects")
    List<Project> all() {
        return repository.findAll();
    }

    @Operation(summary = "Add a project")
    @PostMapping("/projects")
    Project newProject(@RequestBody Project newProject) {
        return repository.save(newProject);
    }
}
