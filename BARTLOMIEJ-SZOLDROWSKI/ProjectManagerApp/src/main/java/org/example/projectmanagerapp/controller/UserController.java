package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name="Users", description = "Operation for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET: Pobierz wszystkich użytkowników
    @GetMapping("/all")
    @Operation(summary = "Retrieve all users", description = "Returns a list of all registered users")
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // POST: add new user
    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Saves a new user in the system")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    //PUT: update user
    @PutMapping
    @Operation(summary = "Update existing user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        updateUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //DELETE: remove user
    @Operation(summary = "Delete user by ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of user to delete") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @Parameter(description = "ID of user to retrieve") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}