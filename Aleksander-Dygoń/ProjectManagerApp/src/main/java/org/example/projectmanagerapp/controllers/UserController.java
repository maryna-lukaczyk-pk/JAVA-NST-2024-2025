package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Controller userów")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Zwraca wszystkich userów", description = "Zwraca liste użytkoników z bazy danych")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz usera po ID", description = "Zwraca pojedynczego usera na podstawie jego ID")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID usera do pobrania") @PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Dodaj nowego usera", description = "Dodaje nowego usera do bazy danych")
    public ResponseEntity<User> createUser(
            @Parameter(description = "JSON usera do dodania") @RequestBody User user) {
        // Niech baza danych przypisze ID
        user.setId(null);
        User savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj usera", description = "Aktualizuje istniejącego usera na podstawie ID")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID usera do aktualizacji") @PathVariable Integer id,
            @Parameter(description = "Zaktualizowane dane usera") @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń usera", description = "Usuwa usera na podstawie podanego ID")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID usera do usunięcia") @PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}