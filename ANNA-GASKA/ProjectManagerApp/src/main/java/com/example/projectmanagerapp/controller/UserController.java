package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(
        name = "User Management",
        description = "APIs for managing users"
)

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieve a list of all users"
    )
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Create a new user with the provided details"
    )


    public ResponseEntity <User> addUser(
            @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

}