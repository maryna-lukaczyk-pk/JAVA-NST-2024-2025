package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

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

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz użytkownika po ID")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Utwórz nowego użytkownika")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj użytkownika")
    public ResponseEntity<Users> updateUser(@PathVariable Long id, @RequestBody Users userDetails) {
        Users updated = userService.updateUser(id, userDetails);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń użytkownika")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean exists = userService.getUserById(id).isPresent();
        if (!exists) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}