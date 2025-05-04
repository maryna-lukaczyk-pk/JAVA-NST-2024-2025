package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user")
    public Users createUser(
            @Parameter(description = "User to be created", required = true)
            @RequestBody Users user) {
        return userService.createUser(user);
    }

    // ========================
// Metoda PUT (aktualizacja)
// ========================
    @PutMapping("/{id}")
    @Operation(
            summary = "Update existing user",
            description = "Updates an existing user by its ID"
    )
    public Users updateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @RequestBody Users updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // ==========================
// Metoda DELETE (usuwanie)
// ==========================
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user by ID",
            description = "Deletes the user with the specified ID"
    )
    public void deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
    }
}