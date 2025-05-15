package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.entity.ProjectUsers;
import com.example.demo.service.ProjectService;
import com.example.demo.service.ProjectUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Project Controller")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectUsersService projectUsersService;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Returns all users projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Saves project", description = "Saves current project")
    @PostMapping
    public Project createProject(
            @Parameter(description = "Project object that needs to be saved", required = true)
            @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a project", description = "Updates a project by its ID")
    public Project updateProject(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated project object", required = true)
            @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a project", description = "Deletes a project by its ID")
    public void deleteProject(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @GetMapping("/{id}/users")
    @Operation(summary = "Get users by project", description = "Returns all users assigned to a project")
    public ResponseEntity<List<ProjectUsers>> getUsersByProject(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable Long id) {
        List<ProjectUsers> users = projectUsersService.getUsersByProject(id);
        if (users == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{id}/users")
    @Operation(summary = "Assign user to project", description = "Assigns a user to a project")
    public ResponseEntity<ProjectUsers> assignUserToProject(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID of the user to assign", required = true)
            @RequestParam Long userId) {
        ProjectUsers projectUsers = projectUsersService.assignUserToProject(id, userId);
        if (projectUsers == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(projectUsers);
    }

    @DeleteMapping("/{id}/users/{userId}")
    @Operation(summary = "Remove user from project", description = "Removes a user from a project")
    public ResponseEntity<Void> removeUserFromProject(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable Long id,
            @Parameter(description = "ID of the user to remove", required = true)
            @PathVariable Long userId) {
        projectUsersService.removeUserFromProject(id, userId);
        return ResponseEntity.ok().build();
    }
}
