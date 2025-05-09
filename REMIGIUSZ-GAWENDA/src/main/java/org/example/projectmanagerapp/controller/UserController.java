package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Tag(name = "User API", description = "User management operations")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
