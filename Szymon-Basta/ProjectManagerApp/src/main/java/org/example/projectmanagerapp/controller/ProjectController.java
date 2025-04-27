package org.example.projectmanagerapp.controller;


import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
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
    public Projects createProject(@RequestBody Projects project) {
        return projectRepository.save(project);
    }
}
