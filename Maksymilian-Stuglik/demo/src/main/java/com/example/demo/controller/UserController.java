package com.example.demo.controller;

import com.example.demo.entity.Users;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Get users projects", description = "Returns all users")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Saves user", description = "Saves current user")
    public Users createUser(
            @Parameter(description = "User object that needs to be saved", required = true)
            @RequestBody Users user) {
        return userService.createUser(user);
    }
}