package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(
        name = "Project",
        description = "Operations for managing projects"
)
public class ProjectController
{
    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(
            method = "GET",
            summary = "Retrieve all projects",
            description = "Returns a list of all projects from the database"
    )
    public ResponseEntity<List<Project>> getAllProjects()
    {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    @Operation(
            method = "GET",
            summary = "Retrieve project by id",
            description = "Returns a project from the database with a matching id or null"
    )
    @Parameter(
            name = "id",
            description = "Project id",
            required = true,
            example = "1"
    )
    public ResponseEntity<Project> getProjectById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(projectService.getProject(id));
    }

    @PostMapping
    @Operation(
            method = "POST",
            summary = "Create a new project",
            description = "Creates a new project in the database from request data and returns it"
    )
    public ResponseEntity<Project> createProject(
            @RequestBody Project project
    ) {
        project.setId(null);
        Project newProject = projectService.createProject(project);

        return ResponseEntity.ok(newProject);
    }

    @PutMapping
    @Operation(
            method = "PUT",
            summary = "Update a project",
            description = "Updates an existing project with values from request body and returns it"
    )
    public ResponseEntity<Project> updateProject(
            @RequestBody Project project
    ) {
        Project updatedProject = projectService.updateProject(project);

        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping
    @Operation(
            method = "DELETE",
            summary = "Delete a project",
            description = "Deletes a project from the database with a matching id and returns it"
    )
    public ResponseEntity<Project> deleteProject(
            Long id
    ) {
        Project deletedProject = projectService.deleteProject(id);

        return ResponseEntity.ok(deletedProject);
    }
}
