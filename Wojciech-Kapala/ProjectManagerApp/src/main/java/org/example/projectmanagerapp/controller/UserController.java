package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create and save a new user")
    public User createUser(@Parameter(description = "User entity to be created") @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Update user by ID")
    public User updateUser(
            @Parameter(description = "ID of user to update") @PathVariable Long id,
            @Parameter(description = "Updated user data") @RequestBody User dto
    ) {
        return userService.updateUser(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete user by ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of user to delete") @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a user", description = "Retrieve a user by ID")
    public User getUserById(
            @Parameter(description = "ID of user to retrieve") @PathVariable Long id
    ) {
        return userService.getUserById(id);
    }
}
