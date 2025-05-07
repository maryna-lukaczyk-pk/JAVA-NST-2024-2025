package com.example;

import com.example.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Operations related to managing projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }


    @PostMapping
    @Operation(summary = "Create a new project", description = "Add a new project to the system")
    public Project createProject( @Parameter(description = "Project object to be created", required = true) @RequestBody Project project) {
        return projectService.save(project);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a single project by its ID")
    public Project getProjectById(
            @Parameter(description = "ID of the project to retrieve", required = true) @PathVariable Long id) {
        return projectService.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Update an existing project by its ID")
    public Project updateProject(
            @Parameter(description = "ID of the project to update", required = true) @PathVariable Long id,
            @Parameter(description = "Updated project object", required = true) @RequestBody Project project) {
        return projectService.update(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Delete a project by its ID")
    public void deleteProject(
            @Parameter(description = "ID of the project to delete", required = true) @PathVariable Long id) {
        projectService.delete(id);
    }
}