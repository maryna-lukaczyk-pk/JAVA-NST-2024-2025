package org.example.projectmanagerapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User")
@RestController
public class UserController {
    private final UserRepository repository;

    UserController(UserRepository userRepository) {
        this.repository = userRepository;
    }

    @Operation(summary = "API User POST method")
    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @Operation(summary = "API User POST method")
    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }
}