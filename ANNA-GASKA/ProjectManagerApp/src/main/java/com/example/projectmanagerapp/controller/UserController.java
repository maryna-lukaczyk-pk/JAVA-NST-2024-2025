package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.List;

@Tag(
        name = "User Management",
        description = "APIs for managing users"
)

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users"
    )
    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Operation(
            summary = "Create a new user",
            description = "Create a new user with the provided details"
    )

    @PostMapping
    public User createUser(
            @Parameter(
                    name = "user",
                    description = "User object to be created",
                    required = true
            )
            @RequestBody User user) {
        return userRepository.save(user);
    }

}