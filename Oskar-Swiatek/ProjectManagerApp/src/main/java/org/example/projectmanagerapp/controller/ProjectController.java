package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Projects", description = "Operations related to projects")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Operation(summary = "Retrieve all projects")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Operation(summary = "Retrieve a project by ID")
    @GetMapping("/{id}")
    @Parameter(description = "ID of the project to retrieve")
    public Project getProjectById(@PathVariable Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Operation(summary = "Create a new project")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @Operation(summary = "Update an existing project")
    @PutMapping("/{id}")
    @Parameter(description = "ID of the project to update")
    public Project updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(projectDetails.getName());
        return projectRepository.save(project);
    }

    @Operation(summary = "Delete a project by ID")
    @DeleteMapping("/{id}")
    @Parameter(description = "ID of the project to delete")
    public void deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }
}