package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name="User Controller", description="Users management")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary="Get all users", description="Receives all users stored")
    @GetMapping("/get-all-users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Operation(summary="Get user by id", description="Receives user with provided id")
    @GetMapping("/get-user/{id}")
    public Optional<User> getByID(
            @Parameter(description="User id") @PathVariable Long id
    ) {
        return userRepository.findById(id);
    }

    @Operation(summary="Creates new user", description="Creates new user")
    @PostMapping("/create-user")
    public User create(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }
}
