package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz projekt po ID")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Utwórz nowy projekt")
    public Project createProject(@RequestBody @Parameter(description = "Dane nowego projektu") Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj projekt")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        Project updated = projectService.updateProject(id, projectDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń projekt")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        boolean exists = projectService.getProjectById(id).isPresent();
        if (!exists) {
            return ResponseEntity.notFound().build();
        }
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}