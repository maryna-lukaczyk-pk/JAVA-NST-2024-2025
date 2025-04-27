package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping("/create")
    public Project createProject(@RequestBody Project project) {
        return projectRepository.save(project);
    }

//    @GetMapping("/{id}")
//    public Project getProjectById(@PathVariable Long id) {
//        return projectRepository.findById(id).orElse(null);
//    }
}