package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-users")
@Tag(name = "Project Users", description = "Operations related to user participation in projects")
public class ProjectUserController {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserController(ProjectUserRepository projectUserRepository) {
        this.projectUserRepository = projectUserRepository;
    }

    @GetMapping
    @Operation(summary = "Retrieve all project-user relationships", description = "Returns a list of all project-user assignments.")
    public List<ProjectUser> getAllProjectUsers() {
        return projectUserRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new project-user relationship", description = "Adds a new association between a user and a project.")
    public ProjectUser createProjectUser(
            @Parameter(description = "ProjectUser object to be created")
            @RequestBody ProjectUser projectUser
    ) {
        return projectUserRepository.save(projectUser);
    }
}
