package org.example.projectmanagerapp.controller;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/Projects")
public class ProjectsController {
    private final ProjectRepository projectRepository;

    public ProjectsController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Projects> createProject(@RequestBody Projects project) {
        Projects savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);
    }
}
