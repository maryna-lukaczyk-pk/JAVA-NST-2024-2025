package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<User> createUser(
        @RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user by ID")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Parameter(description = "Updated user object") @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user by ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
