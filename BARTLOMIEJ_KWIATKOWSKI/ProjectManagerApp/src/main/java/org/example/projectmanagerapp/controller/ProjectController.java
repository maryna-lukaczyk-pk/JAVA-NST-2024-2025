package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.ProjectService;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operacje związane z projektami")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;


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
    @PutMapping("/{projectId}/users/{userId}")
    @Operation(summary = "Przypisz użytkownika do projektu")
    public ResponseEntity<Project> addUserToProject(@PathVariable Long projectId, @PathVariable Long userId) {
        Optional<Project> projectOpt = projectService.getProjectById(projectId);
        Optional<Users> userOpt = userService.getUserById(userId);
        if (projectOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project project = projectOpt.get();
        project.getUsers().add(userOpt.get());
        Project updated = projectService.createProject(project);
        return ResponseEntity.ok(updated);
    }
}