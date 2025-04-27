package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class ProjectController {

    private ProjectRepository projectRepository;

    @GetMapping
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @GetMapping
    public Optional<Project> getByID(@PathVariable Long id) {
        return projectRepository.findById(id);
    }

    @PostMapping
    public Project create(@RequestBody Project newProject) {
        return projectRepository.save(newProject);
    }
}