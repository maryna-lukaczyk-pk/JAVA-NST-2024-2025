package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createProject(@RequestBody Project project) {
        Project savedProject = projectRepository.save(project);

        Map<String, String> response = new HashMap<>();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProject.getId()).toUri();

        response.put("success", "Project created");
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        if(projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            response.put("success", "Project deleted");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateProject(@RequestBody Project project, @PathVariable Integer id) {
        if(projectRepository.existsById(id)) {
            project.setId(id);
            projectRepository.save(project);
            Map<String, String> response = new HashMap<>();
            response.put("success", "Project updated");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
