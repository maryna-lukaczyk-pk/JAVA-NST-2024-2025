package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@Tag(name="Project", description="Opeartions for managing projects")
public class ProjectController {
    private final ProjectRepository projectRepository;

    ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    @Operation(summary="List all projects", description="Returns list of all projects")
    List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @PostMapping("/projects")
    @Operation(summary="Add new project", description="Creates new project")
    Project addProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }
}
