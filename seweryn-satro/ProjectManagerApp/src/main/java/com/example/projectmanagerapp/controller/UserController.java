package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.projectmanagerapp.service.UserService;
import java.util.List;

@RestController
@Tag(name = "Users", description = "Managing users")
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService= userService;
    }

    @GetMapping("/all")
    @Operation(summary = "Users list", description = "Returns the list of every user from database")
    public List<User> allUsers() {
        return userService.allUsers();
    }

    @PostMapping("/create")
    @Operation(summary = "Add new user", description = "Add new user to the database")
    public User newUser(@RequestBody User user) {
        return userService.newUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update user data based on user ID")
    public User updateUser(
            @Parameter(description = "User ID")
            @PathVariable Integer id,
            @RequestBody User newUser) {

        return userService.update(id, newUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete user data based on user ID")
    public void deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Integer id) {
        userService.delete(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Return user by ID")
    public User userById(@PathVariable Integer id) {
        return userService.userById(id);
    }


}