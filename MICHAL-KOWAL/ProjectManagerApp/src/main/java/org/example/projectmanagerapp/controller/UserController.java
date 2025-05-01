package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User controller")
@RestController
@RequestMapping("/api/users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users", description = "Gets all users")
    List<User> all() {
        return userService.getAllUsers();
    }

    @PostMapping("/")
    @Operation(summary = "Create user", description = "Creates a new user")
    User newUser(@RequestBody User newUser)
    {
        return userService.createUser(newUser);
    }
}
