package com.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to managing users")
public class UserController {
    @Autowired
    private UserService userService;

    // GET: Pobierz wszystkich użytkowników
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // POST: Dodaj nowego użytkownika
    @PostMapping
    @Operation(summary = "Create a new user", description = "Add a new user to the system")
    public User createUser(@Parameter(description = "User object to be created", required = true) @RequestBody User user) {
        return userService.createUser(user);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
    public Optional<User> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Update an existing user by their ID")
    public Optional<User> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user object", required = true)
            @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete a user by their ID")
    public void deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
    }
}