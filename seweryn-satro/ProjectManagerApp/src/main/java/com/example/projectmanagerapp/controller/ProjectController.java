package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@Tag(name = "Projects", description = "Managing projects")
@RequestMapping("api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    @Operation(summary = "Projects list", description = "Returns the list of every project")
    public List<Project> allProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping("/create")
    @Operation(summary = "Add new project", description = "Add new project to the database")
    public Project newProject(@RequestBody Project project) {
        return projectService.addProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Update project data based on project ID")
    public Project updateProject(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Integer id,
            @RequestBody Project newProject) {
        return projectService.updateProject(id, newProject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Delete project data based on project ID")
    public void deleteProject(
            @Parameter(description = "Project ID", required = true)
            @PathVariable Integer id) {
        projectService.deleteProject(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Return project by ID")
    public Project projectById(@PathVariable Integer id) {
        return projectService.projectById(id);
    }

}