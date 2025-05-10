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
}