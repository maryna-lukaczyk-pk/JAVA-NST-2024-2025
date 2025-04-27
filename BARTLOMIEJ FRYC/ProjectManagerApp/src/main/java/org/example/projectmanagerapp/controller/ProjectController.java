package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@Tag(name = "Projects", description = "API for managing projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves a list of all projects in the system")
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project with the provided details")
    public Project createProject(
            @Parameter(description = "Project object with details to be created") 
            @RequestBody Project project) {
        return projectRepository.save(project);
    }
}
