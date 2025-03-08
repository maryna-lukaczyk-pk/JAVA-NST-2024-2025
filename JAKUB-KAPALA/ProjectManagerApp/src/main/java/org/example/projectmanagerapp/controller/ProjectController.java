package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private static final String PROJECT_ID_PATH = "/{projectId}";

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @GetMapping(PROJECT_ID_PATH)
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElse(null);

        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectRepository.save(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping(PROJECT_ID_PATH)
    public ResponseEntity<Project> updateProject(@PathVariable Long projectId, @RequestBody Project projectUpdateRequest) {
        Project project =  projectRepository.findById(projectId)
                .map(existingProject -> saveUpdatedProjectDetails(existingProject, projectUpdateRequest))
                .orElse(null);

        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation was not successful");
        }

        return ResponseEntity.ok(project);
    }

    @DeleteMapping(PROJECT_ID_PATH)
    public ResponseEntity<Object> deleteProject(@PathVariable Long projectId) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    projectRepository.delete(project);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private Project saveUpdatedProjectDetails(Project existingProject, Project projectUpdateRequest) {
        existingProject.setName(projectUpdateRequest.getName());
        return projectRepository.save(existingProject);
    }
}