package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@Tag(name = "Users", description = "Managing users")
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Users list", description = "Returns the list of every user")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Add new user", description = "Add new user to the database")
    public User newUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user data based on user ID")
    public User updateUser(
            @Parameter(description = "User ID")
            @PathVariable Integer id,
            @RequestBody User newUser) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setProjects(newUser.getProjects());
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user data based on user ID")
    public void deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Integer id) {

        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

}