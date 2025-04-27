package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create and save a new user")
    public User createUser( @Parameter(description = "User entity to be created") @RequestBody User user) {
        return userRepository.save(user);
    }
}
