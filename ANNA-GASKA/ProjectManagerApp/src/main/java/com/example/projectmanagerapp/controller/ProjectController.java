package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(
        name = "Project Management",
        description = "APIs for managing projects"
)

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Operation(
            summary = "Get all projects",
            description = "Retrieve a list of all projects"
    )

    @GetMapping
    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    @Operation(
            summary = "Create a new project",
            description = "Create a new project with the provided details"
    )

    @PostMapping
    public Projects createProject(
            @Parameter(
                    name = "project",
                    description = "Project object to be created",
                    required = true
            )
            @RequestBody Projects project) {
        return projectRepository.save(project);
    }




}