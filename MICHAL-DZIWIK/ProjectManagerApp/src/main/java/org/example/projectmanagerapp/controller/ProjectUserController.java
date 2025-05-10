package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.service.ProjectUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-users")
@Tag(name = "Project Users", description = "Operations related to user participation in projects")
public class ProjectUserController {

    private final ProjectUserService projectUserService;

    public ProjectUserController(ProjectUserService projectUserService) {
        this.projectUserService = projectUserService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all project-user relationships", description = "Returns a list of all project-user assignments.")
    public List<ProjectUser> getAllProjectUsers() {
        return projectUserService.getAllProjectUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new project-user relationship", description = "Adds a new association between a user and a project.")
    public ProjectUser createProjectUser(
            @Parameter(description = "ProjectUser object to be created")
            @RequestBody ProjectUser projectUser
    ) {
        return projectUserService.createProjectUser(projectUser);
    }

    // Pobranie project-user po ID
    @GetMapping("/{id}")
    @Operation(summary = "Get project-user by ID", description = "Returns a project-user relationship by its ID.")
    public ProjectUser getProjectUserById(
            @Parameter(description = "ID of the project-user relationship to be retrieved")
            @PathVariable Long id
    ) {
        return projectUserService.getProjectUserById(id);
    }

    // Aktualizacja project-user po ID
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project-user relationship", description = "Updates an existing project-user relationship by its ID.")
    public ProjectUser updateProjectUser(
            @Parameter(description = "ID of the project-user relationship to be updated")
            @PathVariable Long id,
            @Parameter(description = "Updated project-user object")
            @RequestBody ProjectUser projectUser
    ) {
        return projectUserService.updateProjectUser(id, projectUser);
    }

    // Usuwanie project-user po ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project-user relationship", description = "Deletes a project-user relationship by its ID.")
    public void deleteProjectUser(
            @Parameter(description = "ID of the project-user relationship to be deleted")
            @PathVariable Long id
    ) {
        projectUserService.deleteProjectUser(id);
    }
}
