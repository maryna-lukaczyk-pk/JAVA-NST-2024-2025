package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje - uzytkownicy")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca użytkowników")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Dodaj nowego użytkownika", description = "Tworzy użytkownika")
    public User createUser(@Parameter(description = "Powstaje nowy uzytkownik") @RequestBody User user) {
        return userRepository.save(user);
    }
}
