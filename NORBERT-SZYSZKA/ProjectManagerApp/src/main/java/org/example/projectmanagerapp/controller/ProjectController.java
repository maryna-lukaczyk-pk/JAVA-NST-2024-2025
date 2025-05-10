package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Project", description = "Project management")
public class ProjectController {

    // projectRepository field declaration
    // "final" means that a field needs to be initialized
    // using constructor and cannot be changed later
    private final ProjectService projectService;

    // constructor for ProjectController class
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns list of all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project")
    public Project createProject(
            @Parameter(description = "Project to be created" ,required = true)
            @RequestBody Project project
    ) {
        return projectService.createProject(project);
    }
}