package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@Tag(name = "Project", description = "Managing projects")
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    @Operation(summary = "Projects list", description = "Returns the list of every project")
    public List<Project> allProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Add new project", description = "Add new project to the database")
    public Project newProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }
}
