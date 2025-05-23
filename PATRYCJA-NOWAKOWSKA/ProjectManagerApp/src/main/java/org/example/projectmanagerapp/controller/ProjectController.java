package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operacje na projektach")
public class ProjectController {

    private ProjectService projectService;

    @Autowired
    private ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation (summary = "Pobierz wszystkie projekty", description = "Zwraca liste wszystkich zapisanych projektów")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation (summary = "Utwórz nowy projekt", description = "Dodaje nowy projekt")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj projekt", description = "Aktualizuje dane projektu na podstawie ID")
    public Project updateProject(
            @Parameter(description = "ID projektu do aktualizacji", required = true)
            @PathVariable Long id,
            @RequestBody Project project
    ) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń projekt", description = "Usuwa projekt na podstawie ID")
    public void deleteProject(
            @Parameter(description = "ID projektu do usunięcia", required = true)
            @PathVariable Long id
    ) {
        projectService.deleteProject(id);
    }
}
