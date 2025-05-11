package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.interfaces.ProjectResponseDTO;
import org.example.projectmanagerapp.interfaces.UserResponseDTO;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User API")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody String username) {
        UserResponseDTO newUser = userService.registerUser(username);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody String username) {
        UserResponseDTO user = userService.loginUser(username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/projects")
    public ResponseEntity<List<ProjectResponseDTO>> getUserProjects(@PathVariable Long userId) {
        List<ProjectResponseDTO> projects = userService.getUserProjects(userId);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<Void> associateUserWithProject(@PathVariable Long userId, @PathVariable Long projectId) {
        userService.associateUserWithProject(userId, projectId);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{userId}/projects/{projectId}")
    public ResponseEntity<Void> removeUserFromProject(@PathVariable Long userId, @PathVariable Long projectId) {
        userService.removeUserFromProject(userId, projectId);
        return ResponseEntity.ok(null);
    }
}
