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


    @GetMapping("/{id}")
    @Operation(summary = "Pobierz projekt po ID")
    public Project getProjectById(
            @Parameter(description = "ID projektu") @PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PostMapping
    @Operation (summary = "Utwórz nowy projekt", description = "Dodaje nowy projekt")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Zaktualizuj projekt po ID")
    public Project updateProject(
            @Parameter(description = "ID projektu do aktualizacji") @PathVariable Long id,
            @RequestBody Project projectDetails) {
        return projectService.updateProject(id, projectDetails);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń projekt po ID")
    public void deleteProject(
            @Parameter(description = "ID projektu do usunięcia") @PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
