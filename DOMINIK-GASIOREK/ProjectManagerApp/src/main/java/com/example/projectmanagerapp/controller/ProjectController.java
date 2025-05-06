package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Project Controller", description="Projects management")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary="Get all projects", description="Receives all projects stored")
    @GetMapping("/get-all-projects")
    public List<Project> getAll() {
        return projectService.getAll();
    }

    @Operation(summary="Get project by id", description="Receives project with provided id")
    @GetMapping("/get-project/{id}")
    public Optional<Project> getByID(
            @Parameter(description="Project id") @PathVariable Long id
    ) {
        return projectService.getByID(id);
    }

    @Operation(summary="Creates new project", description="Creates new project")
    @PostMapping("/create-project")
    public Project create(@RequestBody Project newProject) {
        return projectService.create(newProject);
    }
}
