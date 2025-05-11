package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping("/all")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    @Parameter(name = "id", description = "ID of the user to retrieve", required = true)
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id).orElse(null);
    }

    @Operation(summary = "Create new user", description = "Create a new user with provided details")
    @PostMapping("/create")
    public Users createUser(
            @Parameter(name = "user", description = "User object to create", required = true)
            @RequestBody Users user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Update user", description = "Update an existing user by their ID")
    @Parameter(name = "id", description = "ID of the user to update", required = true)
    @PutMapping("/update/{id}")
    public Users updateUser(
            @PathVariable Long id,
            @Parameter(name = "user", description = "Updated user object", required = true)
            @RequestBody Users user) {
        user.setId(id);
        return userService.updateUser(id, user);
    }

    @Operation(summary = "Delete user", description = "Delete a user by their ID")
    @Parameter(name = "id", description = "ID of the user to delete", required = true)
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
