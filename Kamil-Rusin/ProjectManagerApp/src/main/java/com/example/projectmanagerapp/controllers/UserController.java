package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Users", description = "Operations to manage users")
@RestController
@RequestMapping("api/users")
public class UserController {
    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all users")
    @GetMapping("/all")
    public List<User> getAll() {
        return userService.getAll();
    }

    @Operation(summary = "Retrieve user by Id")
    @GetMapping("/{id}")
    public Optional<User> getUserById(
            @Parameter(description = "Id of the user to retrieve", example = "5")
            @PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Save user to database")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Update user by Id")
    @PutMapping("/update/{id}")
    public User updateUser(
            @Parameter(description = "Id of the user to update", example = "5")
            @PathVariable("id") Long id,
            @RequestBody User user) {
        return userService.updateUser(id, user);
    }
    @Operation(summary = "Delete user from database")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Id of the user to delete", example = "7")
            @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
