

package org.jerzy.projectmanagerapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

import org.jerzy.projectmanagerapp.entity.Project;
import org.jerzy.projectmanagerapp.repository.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectRepository projectRepository;
    
    public ProjectController(ProjectRepository repository) {
        this.projectRepository = repository;
    }
    
    @GetMapping    
    public List<Project> get() {
        return projectRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Project> post(@RequestBody Project project) {
        Project newProject = this.projectRepository.save(project);
        
        return new ResponseEntity<>(newProject, HttpStatus.OK);
    }
    
}