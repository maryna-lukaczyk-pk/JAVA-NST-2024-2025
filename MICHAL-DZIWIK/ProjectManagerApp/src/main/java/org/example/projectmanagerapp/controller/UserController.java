// File: UserController.java
package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations with users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Metoda GET do pobierania wszystkich użytkowników
    @GetMapping
    @Operation(summary = "Get all users", description = "Return list of all users form database.")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Metoda POST do tworzenia nowego użytkownika
    @PostMapping
    @Operation(summary = "Create new users", description = "Add new user to database.")
    public User createUser(
            @Parameter(description = "User object to create")
            @RequestBody User user
    ) {
        return userRepository.save(user);
    }
}