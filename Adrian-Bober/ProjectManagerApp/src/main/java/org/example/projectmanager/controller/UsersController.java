package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Project;
import org.example.projectmanager.entity.Users;
import org.example.projectmanager.service.ProjectUsersService;
import org.example.projectmanager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
@Tag (name = "Users",description = "Operations for managing users")

public class UsersController {
    private final UsersService usersService;
    private final ProjectUsersService projectUsersService;

    @Autowired
    public UsersController(UsersService usersService, ProjectUsersService projectUsersService) {
        this.usersService = usersService;
        this.projectUsersService = projectUsersService;
    }

    @GetMapping
    @Operation (summary = "Retrieve all users",description = "Return a list of all users from the database")
    public List<Users> getAllUsers() {
        return usersService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user",description = "Creates a new user and saves it to the database")
    public ResponseEntity<Users> addUsers(
            @Parameter(description = "User object that needs to be created", required = true)
            @RequestBody Users users) {
        Users createdUser = usersService.createUsers(users);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates an existing user by ID")
    public ResponseEntity<Users> updateUsers(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @RequestBody Users users) {
        Users updatedUsers = usersService.updateUsers(id, users);
        return ResponseEntity.ok(updatedUsers);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    public ResponseEntity<Void> deleteUsers(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id) {
        usersService.deleteUsers(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/projects")
    @Operation(summary = "Get all projects for user")
    public ResponseEntity<List<Project>> getUsersProjects(@PathVariable Long userId) {
        List<Project> projects = projectUsersService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }
}