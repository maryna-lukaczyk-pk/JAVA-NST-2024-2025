package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController
{
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects()
    {
        return ResponseEntity.ok(projectRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(projectRepository.findById(id).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
        @RequestBody Project project
    ) {
        project.setId(null);
        Project newProject = projectRepository.save(project);

        return ResponseEntity.ok(newProject);
    }
}
