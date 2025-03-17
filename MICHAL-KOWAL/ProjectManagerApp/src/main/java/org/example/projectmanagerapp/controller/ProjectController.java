package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/all")
    List<Project> all() {
        return projectRepository.findAll();
    }

    @PostMapping("/")
    Project newProject(@RequestBody Project newProject) {
        return projectRepository.save(newProject);
    }
}
