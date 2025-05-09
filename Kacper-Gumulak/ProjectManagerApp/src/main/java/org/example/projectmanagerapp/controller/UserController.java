package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.dto.UserDTO;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User related operations")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping
    @Operation(summary = "Download all users", description = "Returns a list of all users")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public UserDTO getUserById(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @Operation(summary = "Add new user", description = "Creates a new user based on the data provided")
    public UserDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/{userId}/projects/{projectId}")
    @Operation(summary = "Assign user to project")
    public UserDTO assignToProject(
            @Parameter(description = "User ID", example = "1") @PathVariable Long userId,
            @Parameter(description = "Project ID", example = "1") @PathVariable Long projectId) {
        return userService.assignUserToProject(userId, projectId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates the user data with the given ID")
    public UserDTO updateUser(
            @Parameter(description = "User ID to be updated", example = "1")
            @PathVariable Long id,
            @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes the user with the given ID")
    public void deleteUser(
            @Parameter(description = "User ID to be deleted", example = "1")
            @PathVariable Long id) {
        userService.deleteUser(id);
    }
}
