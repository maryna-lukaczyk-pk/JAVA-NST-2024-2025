package com.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to managing users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // GET: Pobierz wszystkich użytkowników
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // POST: Dodaj nowego użytkownika
    @PostMapping
    @Operation(summary = "Create a new user", description = "Add a new user to the system")
    public User createUser(@Parameter(description = "User object to be created", required = true) @RequestBody User user) {
        return userRepository.save(user);
    }
}