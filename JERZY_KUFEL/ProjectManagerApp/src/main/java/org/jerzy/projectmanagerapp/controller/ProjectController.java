package org.jerzy.projectmanagerapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.jerzy.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Project", description = "Project managment methods")
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService service;
    
    public ProjectController(ProjectRepository repository) {
        this.service = new ProjectService(repository);
    }
    
    @Operation(summary = "List all projects")
    @GetMapping
    public List<Project> get() {
        return service.getAllProjects();
    }

    @Operation(summary = "Create new project")
    @PostMapping
    public ResponseEntity<Project> post(@RequestBody Project project) {
        return new ResponseEntity<>(service.create(project), HttpStatus.CREATED);
    }
}