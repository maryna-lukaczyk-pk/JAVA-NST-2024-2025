package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.service.user_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users Controller")
@RestController
@RequestMapping("/api/users")
public class users_controller {

    @Autowired
    private user_service user_service;

    @GetMapping
    @Operation(summary = "Find all users", description = "Returns all users")
    public List<users> getAllUsers() {
        return user_service.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Creating a user", description = "Saving a user")
    public users createUser(
            @Parameter(description = "User object that needs to be saved", required = true)
            @RequestBody users user) {
        return user_service.create_user(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a user", description = "Updates a user by its ID")
    public users updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user object", required = true)
            @RequestBody users user) {
        return user_service.update_user(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a user", description = "Deletes a user by its ID")
    public void deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        user_service.delete_user(id);
    }
}