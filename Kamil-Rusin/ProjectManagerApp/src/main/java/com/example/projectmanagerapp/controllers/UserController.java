package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Users", description = "Operations to manage users")
@RestController
@RequestMapping("api/users")
public class UserController {
    public final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Retrieve all users")
    @GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Operation(summary = "Retrieve user by Id")
    @GetMapping("/{id}")
    public Optional<User> getUserById(
            @Parameter(description = "Id of the user to retrieve", example = "5")
            @PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

    @Operation(summary = "Save user to database")
    @PostMapping
    public User createUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }


}
