package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/update/{id}")
    @Operation(summary = "Update project", description = "Updates project by id")
    @Parameter(name =  "id", description  = "id of the project to update", required = true)
    ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project newProject)
    {
        Project updatedProject;
        try {
            updatedProject = projectService.updateById(id, newProject);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete project", description = "Deletes project by id")
    @Parameter(name = "id", description  = "id of the project to delete", required = true)
    ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        if (!projectService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project", description = "Gets a project by id")
    @Parameter(name = "id", description  = "id of the project", required = true)
    ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project;
        try {
            project = projectService.getProjectById(id);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(project, HttpStatus.OK);
    }
}
