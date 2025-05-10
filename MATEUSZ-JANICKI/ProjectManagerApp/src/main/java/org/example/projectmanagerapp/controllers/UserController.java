package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "Operations for mapping users")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all users", description = "Return a list of all users from database")
    @GetMapping("/all")
    List<User> getUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Create new users", description = "Adds a new user to database")
    @PostMapping("/create")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        User createdUser = userService.createUser(newUser);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user by ID", description = "Updating user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@Parameter(description = "ID of the user to update") @PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Delete user by ID", description = "Deleting user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}