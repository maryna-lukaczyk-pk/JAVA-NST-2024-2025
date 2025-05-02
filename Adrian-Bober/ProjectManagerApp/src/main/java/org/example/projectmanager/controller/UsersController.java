package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.UsersRepository;
import org.example.projectmanager.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/all")
    @Operation (summary = "Retrieve all users",description = "Return a list of all users from the database")
    public List<Users> getUsers() {
        return usersService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Create a new user",description = "Creates a new user and saves it to the database")
    public ResponseEntity<Users> addUser(@RequestBody Users users) {
        Users createdUser = usersService.createUser(users);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}