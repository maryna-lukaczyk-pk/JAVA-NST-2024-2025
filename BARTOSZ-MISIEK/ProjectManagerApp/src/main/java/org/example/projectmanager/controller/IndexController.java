package org.example.projectmanager.controller;

import org.example.projectmanager.entity.project.Project;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/index")
public class IndexController {
    @Autowired
    ProjectRepository projectRepository;

    @GetMapping("/list")
    public String listProjects() {
       return projectRepository.findAll().stream().map(Project::getName).collect(Collectors.joining("\n"));
    }

    @PostMapping("/add/{projectName}")
    public String addProject(@PathVariable String projectName) {
        Project project = new Project();
        project.setName(projectName);
        projectRepository.save(project);
        return project.getName();
    }
}
