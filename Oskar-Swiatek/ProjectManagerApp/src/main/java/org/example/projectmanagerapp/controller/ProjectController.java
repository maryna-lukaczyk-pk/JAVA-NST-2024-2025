package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Projects", description = "Operations related to projects")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Retrieve all projects")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Retrieve a project by ID")
    @GetMapping("/{id}")
    @Parameter(description = "ID of the project to retrieve")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Create a new project")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @Operation(summary = "Update an existing project")
    @PutMapping("/{id}")
    @Parameter(description = "ID of the project to update")
    public Project updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        return projectService.updateProject(id, projectDetails);
    }

    @Operation(summary = "Delete a project by ID")
    @DeleteMapping("/{id}")
    @Parameter(description = "ID of the project to delete")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}