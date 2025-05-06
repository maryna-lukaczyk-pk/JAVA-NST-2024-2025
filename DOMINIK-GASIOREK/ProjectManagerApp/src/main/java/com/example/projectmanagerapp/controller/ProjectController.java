package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="Project Controller", description="Projects management")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Operation(summary="Get all projects", description="Receives all projects stored")
    @GetMapping("/get-all-projects")
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @Operation(summary="Get project by id", description="Receives project with provided id")
    @GetMapping("/get-project/{id}")
    public Optional<Project> getByID(
            @Parameter(description="Project id") @PathVariable Long id
    ) {
        return projectRepository.findById(id);
    }

    @Operation(summary="Creates new project", description="Creates new project")
    @PostMapping("/create-project")
    public Project create(@RequestBody Project newProject) {
        return projectRepository.save(newProject);
    }
}