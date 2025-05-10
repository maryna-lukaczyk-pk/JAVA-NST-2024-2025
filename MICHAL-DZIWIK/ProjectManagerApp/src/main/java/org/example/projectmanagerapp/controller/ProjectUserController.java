// File: ProjectUserController.java
package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project-users")
public class ProjectUserController {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserController(ProjectUserRepository projectUserRepository) {
        this.projectUserRepository = projectUserRepository;
    }

    @GetMapping
    public List<ProjectUser> getAllProjectUsers() {
        return projectUserRepository.findAll();
    }

    @PostMapping
    public ProjectUser createProjectUser(@RequestBody ProjectUser projectUser) {
        return projectUserRepository.save(projectUser);
    }
}