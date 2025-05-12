package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(name = "Users", description = "Operations related to users")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Retrieve all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Retrieve a user by ID")
    @GetMapping("/{id}")
    @Parameter(description = "ID of the user to be retrieved")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Create a new user")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Update an existing user")
    @PutMapping("/{id}")
    @Parameter(description = "ID of the user to be updated")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/{id}")
    @Parameter(description = "ID of the user to be deleted.")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}