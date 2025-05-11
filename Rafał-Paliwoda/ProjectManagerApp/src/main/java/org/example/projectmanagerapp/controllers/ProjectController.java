package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name = "Kontroler odpowiedzialny za zarządzanie projektami")
public class ProjectController {

    private final ProjectService projectService;


    @Autowired
    public ProjectController(
            ProjectService projectService
    ) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za pobieranie projektu po id")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return projectService.findProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(description = "Endpoint odpowiedzialny za pobieranie listy wszystkich projektów")
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.findAllProjects());
    }

    @PostMapping
    @Operation(description = "Endpoint odpowiedzialny za tworzenie projektu")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectService.createProject(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za zmiane wartości pól dla projektu")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za usuwanie projektu")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/tasks")
    @Operation(description = "Endpoint odpowiedzialny za pobieranie listy zadań dla projektu")
    public ResponseEntity<List<Task>> getProjectTasks(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getProjectTasks(projectId));
    }

    @PostMapping("/{projectId}/user")
    @Operation(description = "Endpoint odpowiedzialny za przypisanie użytkownika do projektu")
    public ResponseEntity<Void> assignUserToProject(@PathVariable Long projectId, @RequestBody Long userId) {
        try {
            projectService.assignUserToProject(projectId, userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}