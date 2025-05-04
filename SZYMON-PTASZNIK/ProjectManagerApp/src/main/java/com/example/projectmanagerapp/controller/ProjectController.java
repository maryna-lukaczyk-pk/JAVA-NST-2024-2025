package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.services.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@Tag(name="Project", description="Opeartions for managing projects")
public class ProjectController {
    private final ProjectService projectService;

    ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    @Operation(summary="List all projects", description="Returns list of all projects")
    public List<Project> getProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping("/projects")
    @Operation(summary="Add new project", description="Creates new project")
    public Project createProject(@RequestBody Project project) {return projectService.createProject(project);
    }

    @PutMapping("projects/{id}")
    @Operation(summary = "Update an existing project", description = "Updates the details of an existing project by ID")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("projects/{id}")
    @Operation(summary = "Delete a project", description = "Deletes the project by ID")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
