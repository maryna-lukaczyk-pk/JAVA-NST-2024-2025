package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "Retrieve all users", description = "Returns a list of all users from the database")
    public List<Users> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("users/{id}")
    @Operation(summary = "Get user by ID", description = "Returns user details by ID")
    public Users getUserById(@PathVariable Long id) {return userService.getUserById(id);}


    @PostMapping("/users")
    @Operation(summary = "Create a new user", description = "Adds a new user to the database")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Update an existing user", description = "Updates the details of an existing user by ID")
    public Users updateUser(@PathVariable Long id, @RequestBody Users user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Delete an user", description = "Deletes the user by ID")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}