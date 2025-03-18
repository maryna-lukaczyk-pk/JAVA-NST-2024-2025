package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.repository.projects_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class projects_controller {

    @Autowired
    private projects_repository projects_repo;

    @GetMapping
    public List<projects> getAllProjects() {
        return projects_repo.findAll();
    }


    @PostMapping("/")
    public projects createProject(@RequestBody projects project) {
        return projects_repo.save(project);
    }
}