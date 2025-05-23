package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje związane z użytkownikami")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę wszystkich użytkowników w systemie")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Dodaj nowego użytkownika", description = "Tworzy nowego użytkownika")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
