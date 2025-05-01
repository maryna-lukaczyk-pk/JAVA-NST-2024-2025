package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project", description = "Project controller")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all projects", description = "Gets all projects")
    List<Project> all() { return projectService.getAllProjects(); }

    @PostMapping("/")
    @Operation(summary = "Create project", description = "Creates a new project")
    Project newProject(@RequestBody Project newProject)
    {
        return projectService.createProject(newProject);
    }
}
