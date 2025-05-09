package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all users", description = "Returns a list of all users from the database")
    @GetMapping("/all")
    public List<Users> getUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Create a new user", description = "Creates a user with the provided information")
    @PostMapping("/create")
    public ResponseEntity<Users> addUser(
            @Parameter(description = "User object to be created")
            @RequestBody Users user) {
        Users createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update a user", description = "Updates an existing user by ID")
    @PutMapping("/{id}")
    public Users updateUser(
            @Parameter(description = "ID of the user to be updated") @PathVariable Long id,
            @RequestBody Users user) {
        return userService.updateUser(id, user);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @DeleteMapping("/{id}")
    public void deleteUser(
            @Parameter(description = "ID of the task to be deleted") @PathVariable Long id) {
        userService.deleteUser(id);
    }

}
