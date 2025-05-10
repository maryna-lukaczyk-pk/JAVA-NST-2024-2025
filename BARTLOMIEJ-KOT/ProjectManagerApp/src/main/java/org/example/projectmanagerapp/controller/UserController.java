package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Operacje na użytkownikach")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Pobierz wszystkich użytkowników")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Dodaj nowego użytkownika")
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
}
