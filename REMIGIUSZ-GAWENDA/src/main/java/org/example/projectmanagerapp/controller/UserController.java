package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Tag(name = "User API", description = "User management operations")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Get all users", description = "Retrieve a list of all users")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
