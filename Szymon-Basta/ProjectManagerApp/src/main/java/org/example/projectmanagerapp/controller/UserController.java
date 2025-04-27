package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for managing users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET
    @Operation(summary = "Retrieve all users", description = "Returns a list of all users from the database")
    @GetMapping
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // POST
    @Operation(summary = "Create a new user", description = "Creates a user with the provided information")
    @PostMapping
    public Users createUser(
            @Parameter(description = "User object to be created")
            @RequestBody Users user) {
        return userRepository.save(user);
    }
}
