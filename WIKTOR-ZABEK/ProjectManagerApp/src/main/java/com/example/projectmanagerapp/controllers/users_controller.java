package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.service.user_service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PostMapping("/")
    @Operation(summary = "Creating a user", description = "Saving a user")
    public users createUser(@RequestBody users user){
        return user_service.create_user(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find user by ID", description = "Returns a single user by ID")
    public ResponseEntity<users> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        Optional<users> user = user_service.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates user information by ID")
    public ResponseEntity<users> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable Long id,
            @RequestBody users user) {
        users updatedUser = user_service.updateUser(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable Long id) {
        user_service.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}