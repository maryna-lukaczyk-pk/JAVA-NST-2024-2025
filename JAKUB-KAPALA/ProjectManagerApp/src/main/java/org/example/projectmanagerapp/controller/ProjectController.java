package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project API")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElse(null);

        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }

        return ResponseEntity.ok(project);
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<User>> getAssociatedUsers(@PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        List<User> users = project.getUsers().stream().toList();

        return ResponseEntity.ok(users);
    }

    @PostMapping("/add")
    public ResponseEntity<Project> createProject(@RequestBody String projectName) {
        Project createdProject = projectRepository.save(new Project(projectName));
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/rename/{projectId}")
    public ResponseEntity<Project> renameProject(@PathVariable Long projectId, @RequestBody String projectName) {
        Project project =  projectRepository.findById(projectId)
                .map(existingProject -> {
                    existingProject.setName(projectName);
                    return projectRepository.save(existingProject);
                })
                .orElse(null);

        if (project == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Operation was not successful");
        }

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/remove/{projectId}")
    public ResponseEntity<Object> deleteProject(@PathVariable Long projectId) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    projectRepository.delete(project);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> associateUserWithProject(@PathVariable Long userId, @PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.getUsers().add(user);
        projectRepository.save(project);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long userId, @PathVariable Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.getUsers().remove(user);
        projectRepository.save(project);

        return ResponseEntity.ok(null);
    }
}
