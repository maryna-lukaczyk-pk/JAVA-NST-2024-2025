package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import com.example.projectmanagerapp.service.UserService;
import java.util.List;

@Tag(name = "User Controller", description = "Operations on users - retrieving, creating, updating, deleting information")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a single user by its ID")
    @GetMapping("/{id}")
    public User getUserById(@Parameter(description = "ID of the user to retrieve") @PathVariable int id) {
        return userService.getById(id);
    }

    @Operation(summary = "Create new user", description = "Create a new user with the given data")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @Operation(summary = "Update user", description = "Update an existing user with given ID and data")
    @PutMapping("/{id}")
    public User updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable int id,
            @RequestBody User user) {
        return userService.update(id, user);
    }

    @Operation(summary = "Delete user", description = "Delete an existing user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}