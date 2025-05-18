package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.services.ProjectService;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project", description = "Methods of Project")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @Operation(summary = "Get all projects")
    @GetMapping("/all")
    List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Add a project")
    @PostMapping("/create")
    public ResponseEntity<Project> addProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "Get project by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@Parameter(description = "ID of the project") @PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update project by ID")
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(id, project));
    }

    @Operation(summary = "Delete project by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProjectById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{projectId}/users")
    public ResponseEntity<?> assignUserToProject(@PathVariable Long projectId, @RequestBody Long userId) {
        Optional<Project> optionalProject = projectService.getProjectById(projectId);
        Optional<User> optionalUser = userService.getUserById(userId);
        if (optionalProject.isEmpty() || optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Project project = optionalProject.get();
        User user = optionalUser.get();
        project.getUsers().add(user);
        projectService.updateProject(projectId, project);
        return ResponseEntity.ok().build();
    }

}
