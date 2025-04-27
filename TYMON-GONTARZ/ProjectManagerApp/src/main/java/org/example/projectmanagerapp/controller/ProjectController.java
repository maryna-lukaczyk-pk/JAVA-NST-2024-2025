package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")

public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    // GET
    @GetMapping
    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    // POST
    @PostMapping
    public ResponseEntity<Projects> createProject(@RequestBody Projects project) {
        Projects savedProject = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }
}
