package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations with users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns list of all users from database.")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Adds a new user to the database.")
    public User createUser(
            @Parameter(description = "User object to be created")
            @RequestBody User user
    ) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates an existing user based on ID.")
    public User updateUser(
            @Parameter(description = "ID of the user to be updated")
            @PathVariable Long id,
            @Parameter(description = "Updated user object")
            @RequestBody User user
    ) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user based on ID.")
    public void deleteUser(
            @Parameter(description = "ID of the user to be deleted")
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
    }
}

