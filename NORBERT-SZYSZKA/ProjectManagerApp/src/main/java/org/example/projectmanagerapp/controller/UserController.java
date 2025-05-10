package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
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
    private final UserService userService;

    // constructor for UserController class
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns list of all users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user")
    public User createUser(
            @Parameter(description = "User to be created" ,required = true)
            @RequestBody User user
    ) {
        return userService.createUser(user);
    }
}