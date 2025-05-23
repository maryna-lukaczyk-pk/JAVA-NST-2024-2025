package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje związane z użytkownikami")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę wszystkich użytkowników w systemie")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz użytkownika po ID", description = "Zwraca dane użytkownika na podstawie podanego ID")
    public User getUserById(
            @Parameter(description = "ID użytkownika", required = true)
            @PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    @Operation(summary = "Dodaj nowego użytkownika", description = "Tworzy nowego użytkownika")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj użytkownika", description = "Aktualizuje dane istniejącego użytkownika na podstawie ID")
    public User updateUser(
            @Parameter(description = "ID użytkownika do aktualizacji", required = true)
            @PathVariable("id") Long id,
            @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń użytkownika", description = "Usuwa użytkownika na podstawie ID")
    public void deleteUser(
            @Parameter(description = "ID użytkownika do usunięcia", required = true)
            @PathVariable("id") Long id) {
        userService.deleteUser(id);
    }
}


