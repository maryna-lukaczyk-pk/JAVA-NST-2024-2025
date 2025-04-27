package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@Tag(name = "Users", description = "operations for managing users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "List all users", description = "Returns list of all users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    @Operation(summary = "Add new user", description = "Creates new user")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by ID")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete a user", description = "Deletes the user by ID")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
