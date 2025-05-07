package com.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Operations related to managing projects")
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;


    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve a list of all projects")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }


    @PostMapping
    @Operation(summary = "Create a new project", description = "Add a new project to the system")
    public Project createProject( @Parameter(description = "Project object to be created", required = true) @RequestBody Project project) {
        return projectRepository.save(project);
    }
}