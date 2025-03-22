package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public List<Project> getAllTasks() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Project getTaskById(@PathVariable Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Project createTask(@RequestBody Project project) {
        return projectRepository.save(project);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }
}
