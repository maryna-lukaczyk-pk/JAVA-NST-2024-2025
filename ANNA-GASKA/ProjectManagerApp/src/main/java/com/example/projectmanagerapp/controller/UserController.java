package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(
        name = "User Management",
        description = "APIs for managing users"
)

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users"
    )
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Create a new user with the provided details"
    )


    public ResponseEntity <User> addUser(
            @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by ID")

    public ResponseEntity<User> getUserById(
            @PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing user",
            description = "Update the details of an existing user"
    )
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User userData) {
        User updated = userService.updateUser(id, userData);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Delete a user by ID"
    )
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}