package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.interfaces.ProjectResponseDTO;
import org.example.projectmanagerapp.interfaces.ProjectTasksResponseDTO;
import org.example.projectmanagerapp.interfaces.UserResponseDTO;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project API")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    public List<ProjectResponseDTO> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long projectId) {
        ProjectResponseDTO project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<UserResponseDTO>> getAssociatedUsers(@PathVariable Long projectId) {
        List<UserResponseDTO> users = projectService.getAssociatedUsers(projectId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<ProjectTasksResponseDTO>> getAssociatedTasks(@PathVariable Long projectId) {
        List<ProjectTasksResponseDTO> tasks = projectService.getAssociatedTasks(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/add")
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody String projectName) {
        ProjectResponseDTO createdProject = projectService.createProject(projectName);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/rename/{projectId}")
    public ResponseEntity<ProjectResponseDTO> renameProject(@PathVariable Long projectId, @RequestBody String projectName) {
        ProjectResponseDTO project = projectService.renameProject(projectId, projectName);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/remove/{projectId}")
    public ResponseEntity<Object> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> associateUserWithProject(@PathVariable Long userId, @PathVariable Long projectId) {
        projectService.associateUserWithProject(userId, projectId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long userId, @PathVariable Long projectId) {
        projectService.removeUserFromProject(userId, projectId);
        return ResponseEntity.ok(null);
    }
}
