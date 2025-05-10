package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
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
    private final UserRepository userRepository;

    // constructor for UserController class
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns list of all users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user")
    public User createUser(
            @Parameter(description = "User to be created" ,required = true)
            @RequestBody User user
    ) {
        return userRepository.save(user);
    }
}