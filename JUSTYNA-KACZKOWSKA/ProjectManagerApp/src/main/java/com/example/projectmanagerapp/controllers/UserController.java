package com.example.projectmanagerapp.controllers;


import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "User", description = "Methods of User")
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository repository) {
        this.repository= repository;
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @Operation(summary = "Add a user")
    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }
}
