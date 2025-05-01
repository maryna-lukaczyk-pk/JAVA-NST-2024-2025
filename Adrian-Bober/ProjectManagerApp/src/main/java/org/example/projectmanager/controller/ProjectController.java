package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Project;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/projects")
@Tag (
        name = "Project",
        description = "ProjectController"
)
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @Operation (
            summary = "Test",
            description = "Test desc"
    )

    @GetMapping
    public List<Project> getAllProjects(
            @Parameter(
                    name = "opis",
                    description = "description",
                    required = true
            )
            @RequestParam(required = false) String opis
    )
    {
        return projectRepository.findAll();
    }

    @Operation(
            summary = "Create a new project",
            description = "Creates a new project and saves it to the database"
    )

    @PostMapping
    public Project createProject(
            @Parameter(
                    description = "Project object that needs to be created",
                    required = true
            )
            @RequestBody Project project
    ) {
        return projectRepository.save(project);
    }
}