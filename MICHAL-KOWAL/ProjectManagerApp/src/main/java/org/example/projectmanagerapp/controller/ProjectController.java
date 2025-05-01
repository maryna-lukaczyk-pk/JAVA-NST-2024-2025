package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project", description = "Project controller")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all projects", description = "Gets all projects")
    List<Project> all() { return projectRepository.findAll(); }

    @PostMapping("/")
    @Operation(summary = "Create project", description = "Creates a new project")
    Project newProject(@RequestBody Project newProject)
    {
        return projectRepository.save(newProject);
    }
}
