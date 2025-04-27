package org.jerzy.projectmanagerapp.controller;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "User", description = "User managment methods")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository repository) {
        this.userRepository = repository;
    }

    @Operation(summary = "List all users")
    @GetMapping
    public List<User> get() {
        return userRepository.findAll();
    }

    @Operation(summary = "Create new user")
    @PostMapping("/create")
    public ResponseEntity<User> postMethodName(@RequestBody User user) {
        User newUser = userRepository.save(user);

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }
}
