package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operacje związane z projektami")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(summary = "Pobierz wszystkie projekty")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Utwórz nowy projekt")
    public Project createProject(@RequestBody @Parameter(description = "Dane nowego projektu") Project project) {
        return projectService.createProject(project);
    }
}