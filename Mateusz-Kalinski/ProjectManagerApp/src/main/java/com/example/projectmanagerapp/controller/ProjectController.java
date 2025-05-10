package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Endpoints for managing projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects in the system")
    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Operation(summary = "Get project by ID", description = "Retrieve a specific project by its ID")
    @Parameter(name = "id", description = "ID of the project to retrieve", required = true)
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @Operation(summary = "Create new project", description = "Create a new project with provided details")
    @PostMapping("/create")
    public Project createProject(
            @Parameter(name = "project", description = "Project object to create", required = true)
            @RequestBody Project project) {
        return projectRepository.save(project);
    }

    @Operation(summary = "Update project", description = "Update an existing project by its ID")
    @Parameter(name = "id", description = "ID of the project to update", required = true)
    @PutMapping("/update/{id}")
    public Project updateProject(
            @PathVariable Long id,
            @Parameter(name = "project", description = "Updated project object", required = true)
            @RequestBody Project project) {
        project.setId(id);
        return projectRepository.save(project);
    }

    @Operation(summary = "Delete project", description = "Delete a project by its ID")
    @Parameter(name = "id", description = "ID of the project to delete", required = true)
    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }
}