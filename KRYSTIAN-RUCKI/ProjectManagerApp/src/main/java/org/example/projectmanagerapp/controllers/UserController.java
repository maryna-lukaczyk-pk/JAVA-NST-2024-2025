package org.example.projectmanagerapp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Users", description = "Operations for managing users")
class UserController {

    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository = repository;
    }
    @GetMapping("/users")
    @Operation(summary = "Retrieve all users", description = "Returns a list of all users from the database")
    List<Users> all() {
        return repository.findAll();
    }

    @PostMapping("/users")
    @Operation(summary = "Create a new user", description = "Adds a new user to the database")
    Users newUser(@RequestBody Users newUser) {
        return repository.save(newUser);
    }
}