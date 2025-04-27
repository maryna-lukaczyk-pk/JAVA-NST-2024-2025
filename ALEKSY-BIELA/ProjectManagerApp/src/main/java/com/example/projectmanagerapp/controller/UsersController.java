package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import com.example.projectmanagerapp.repozytorium.UsersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for managing users.")
public class UsersController {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Get user by id
    @GetMapping("/{id}")
    @Operation(
            summary = "Find user by ID",
            description = "Returns a user matching the provided ID."
    )
    public ResponseEntity<Users> getUserById(
            @Parameter(description = "ID of the user to be retrieved") @PathVariable Long id) {
        Optional<Users> user = usersRepository.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the created entity."
    )
    public ResponseEntity<Users> createUser(
            @Parameter(description = "Details of the user to be created") @RequestBody Users user) {
        Users createdUser = usersRepository.save(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
