package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects", description = "Controller dla projektów")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Zwraca wszystkie projekty", description = "Zwraca wszystkie projekty z bazy danych")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz projekt po ID", description = "Zwraca pojedynczy projekt na podstawie jego ID")
    public ResponseEntity<Project> getProjectById(
            @Parameter(description = "ID projektu do pobrania") @PathVariable Integer id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Dodaje nowy projekt", description = "Dodaje nowy projekt do bazy danych")
    public ResponseEntity<Project> createProject(
            @Parameter(description = "JSON project do dodania") @RequestBody Project project) {
        // Baza danych przypisze ID
        project.setId(null);
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.ok(savedProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj projekt", description = "Aktualizuje istniejący projekt na podstawie ID")
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID projektu do aktualizacji") @PathVariable Integer id,
            @Parameter(description = "Zaktualizowane dane projektu") @RequestBody Project projectDetails) {
        Project updatedProject = projectService.updateProject(id, projectDetails);
        if (updatedProject != null) {
            return ResponseEntity.ok(updatedProject);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń projekt", description = "Usuwa projekt na podstawie podanego ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID projektu do usunięcia") @PathVariable Integer id) {
        Optional<Project> project = projectService.getProjectById(id);
        if (project.isPresent()) {
            projectService.deleteProjectById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}