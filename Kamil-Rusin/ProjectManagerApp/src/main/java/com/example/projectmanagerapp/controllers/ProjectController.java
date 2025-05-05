package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Projects", description = "Operations to manage projects")
@RestController
@RequestMapping("api/projects")
public class ProjectController {
    public final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Retrieve all projects")
    @GetMapping("/all")
    public List<Project> getAll() {
        return projectService.getAll();
    }

    @Operation(summary = "Retrieve project by Id")
    @GetMapping("/{id}")
    public Optional<Project> getProjectById(
            @Parameter(description = "Id of the project to retrieve", example = "5")
            @PathVariable("id") Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Save project to database")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }
}


