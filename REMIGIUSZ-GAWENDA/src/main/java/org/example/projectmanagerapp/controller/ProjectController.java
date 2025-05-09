package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Tag(name = "Project API", description = "Project management operations")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    @Operation(summary = "Create a new project", description = "Create a new project in the system")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }
}
