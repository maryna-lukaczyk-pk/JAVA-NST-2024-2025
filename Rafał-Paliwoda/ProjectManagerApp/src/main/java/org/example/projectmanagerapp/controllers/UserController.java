package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "Kontroler odpowiedzialny za zarządzanie użytkownikami")
public class UserController {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public UserController(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za pobieranie użytkownika po id")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(description = "Endpoint odpowiedzialny za pobieranie listy wszystkich użytkowników")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(description = "Endpoint odpowiedzialny za tworzenie użytkownika")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za zmiane pól użytkownika")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Użytkownik o ID " + id + " nie został znaleziony"));

        user.setUsername(userDetails.getUsername());
        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Endpoint odpowiedzialny za usuwanie użytkownika")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Użytkownik o ID " + id + " nie został znaleziony"));

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/project")
    @Operation(description = "Endpoint odpowiedzialny za przydzielenie użytkownika do projektu")
    public ResponseEntity<Void> assignUserToProject(@PathVariable Long userId, @RequestBody Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Użytkownik o ID " + userId + " nie został znaleziony"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projekt o ID " + projectId + " nie został znaleziony"));

        if (project.getUsers() == null) {
            project.setUsers(new java.util.ArrayList<>());
        }

        if (!project.getUsers().contains(user)) {
            project.getUsers().add(user);
            projectRepository.save(project);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/project/{projectId}")
    @Operation(description = "Endpoint odpowiedzialny za usunięcie użytkownika z projektu")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long userId, @PathVariable Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Użytkownik o ID " + userId + " nie został znaleziony"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Projekt o ID " + projectId + " nie został znaleziony"));

        if (project.getUsers() != null && project.getUsers().contains(user)) {
            project.getUsers().remove(user);
            projectRepository.save(project);
        }

        return ResponseEntity.noContent().build();
    }
}