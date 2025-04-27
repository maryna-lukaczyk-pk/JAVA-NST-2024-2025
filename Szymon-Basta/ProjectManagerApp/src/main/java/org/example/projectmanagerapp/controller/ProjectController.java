package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    // GET
    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects from the database")
    @GetMapping
    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    // POST
    @Operation(summary = "Create a new project", description = "Creates a project with the provided information")
    @PostMapping
    public Projects createProject(
            @Parameter(description = "Project object to be created")
            @RequestBody Projects project) {
        return projectRepository.save(project);
    }
}
