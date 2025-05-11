package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "User management")
public class UserController {

    // userRepository field declaration
    // "final" means that a field needs to be initialized
    // using constructor and cannot be changed later
    private final UserService userService;

    // constructor for UserController class
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{all}")
    @Operation(summary = "Get all users", description = "Returns list of all users")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns user by ID")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID of user to be retrieved" ,required = true)
            @PathVariable Long id
    ) {
        return userService.findById(id)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Creates a new user")
    public User createUser(
            @Parameter(description = "User to be created" ,required = true)
            @RequestBody User user
    ) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Updates an existing user")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID of user to be updated" ,required = true)
            @PathVariable Long id,
            @Parameter(description = "New user data" ,required = true)
            @RequestBody User user
    ) {
        return userService.findById(id)
                          .map(existing -> ResponseEntity.ok(userService.update(id, user)))
                          .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an existing user", description = "Deletes an existing user")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of user to be deleted" ,required = true)
            @PathVariable Long id
    ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}