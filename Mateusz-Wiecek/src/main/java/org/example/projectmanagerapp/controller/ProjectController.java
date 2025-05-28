package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Projects", description = "Operations related to projects")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService svc;

    public ProjectController(ProjectService svc) {
        this.svc = svc;
    }

    @Operation(summary = "Get all projects")
    @GetMapping
    public List<Project> getAllProjects() {
        return svc.getAllProjects();
    }

    @Operation(summary = "Get project by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        return svc.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new project")
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project p) {
        Project created = svc.createProject(p);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created); // ZWRACA 201 Created
    }


    @Operation(summary = "Update project by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id,
                                                 @RequestBody Project d) {
        return svc.updateProject(id, d)
                .map(ResponseEntity::ok)  // 200 OK + updated body
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete project by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        return svc.deleteProject(id)
                ? ResponseEntity.noContent().build() // 204 No Content
                : ResponseEntity.notFound().build();
    }
}
