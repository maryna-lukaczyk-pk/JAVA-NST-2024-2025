package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje związane z użytkownikami")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Utwórz nowego użytkownika")
    public Users createUser(@RequestBody @Parameter(description = "Dane nowego użytkownika") Users users) {
        return userService.createUser(users);
    }
}