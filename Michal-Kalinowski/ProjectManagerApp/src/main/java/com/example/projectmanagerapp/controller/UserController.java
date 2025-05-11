package com.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operation for managing users")
public class UserController {

    private final UserService userService;

public UserController(UserService userService) {
    this.userService = userService;
}


    @GetMapping("/all")
    @Operation(summary = "Retrive all users", description = "Return list of all users from the databse")
    public ResponseEntity<List<User>> GetUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Creates a new user in the database")
    public ResponseEntity<User> create(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
