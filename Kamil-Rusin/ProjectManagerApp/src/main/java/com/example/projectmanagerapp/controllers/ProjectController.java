package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.services.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Projects", description = "Operations to manage projects")
@RestController
@RequestMapping("api/projects")
public class ProjectController {
    public final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Retrieve all projects")
    @GetMapping("/all")
    public List<Project> getAll() {
        return projectService.getAll();
    }

    @Operation(summary = "Retrieve project by Id")
    @GetMapping("/{id}")
    public Optional<Project> getProjectById(
            @Parameter(description = "Id of the project to retrieve", example = "5")
            @PathVariable("id") Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Save project to database")
    @PostMapping
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @Operation(summary = "Update project by Id")
    @PutMapping("/update/{id}")
    public Project updateProject(
            @Parameter(description = "Id of the project to update", example = "5")
            @PathVariable("id") Long id,
            @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @Operation(summary = "Delete project from database")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Id of the project to delete", example = "5")
            @PathVariable("id") Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add user to project")
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> assignUserToProject(
            @Parameter(description = "Id of the project", example = "5")
            @PathVariable("projectId") Long projectId,
            @RequestBody Map<String, Long> body) {
        Long userId = body.get("userId");
        projectService.assignUserToProject(userId, projectId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Add task to project")
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<Void> assignTaskToProject(
            @Parameter(description = "Id of the project", example = "5")
            @PathVariable("projectId") Long projectId,
            @RequestBody Map<String, Long> body) {
        Long taskId = body.get("taskId");
        projectService.assignTaskToProject(taskId, projectId);
        return ResponseEntity.ok().build();
    }

}


