package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Create and save a new project")
    public Project createProject(@Parameter(description = "Project entity to be created") @RequestBody Project project) {
        return projectRepository.save(project);
    }
}
