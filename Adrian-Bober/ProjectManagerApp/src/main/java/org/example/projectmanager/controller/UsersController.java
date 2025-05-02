package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
@Tag (
        name = "Users",
        description = "Operations for managing users"
)
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    @Operation (
            summary = "Retrieve all users",
            description = "Return a list of all users from the database"
    )

    @GetMapping
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and saves it to the database"
    )

    @PostMapping
    public Users createUser(
            @Parameter(
                    description = "User that needs to be created",
                    required = true
            )
            @RequestBody Users user
    ) {
        return usersRepository.save(user);
    }
}