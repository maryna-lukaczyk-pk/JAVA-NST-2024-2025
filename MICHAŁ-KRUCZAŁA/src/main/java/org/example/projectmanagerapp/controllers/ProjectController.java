package org.example.projectmanagerapp.controllers;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repositories.ProjectRepository;
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


    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectRepository.save(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }
}