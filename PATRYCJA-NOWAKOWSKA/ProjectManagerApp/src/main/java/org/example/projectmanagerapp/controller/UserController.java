package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.projectmanagerapp.service.UserService;


import java.util.List;



@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje na użytkownikach systemu")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę użytkowników")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz użytkownika po ID", description = "Zwraca dane użytkownika na podstawie ID")
    public User getUserById(
            @Parameter(description = "ID użytkownika", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @PostMapping
    @Operation(
            summary = "Utwórz nowego użytkownika",
            description = "Zapisuje użytkownika w bazie i zwraca jego dane"
    )
    public User createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON z danymi nowego użytkownika",
                    required = true
            )
            @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Aktualizuj użytkownika",
            description = "Aktualizuje dane użytkownika na podstawie ID"
    )
    public User updateUser(
            @Parameter(description = "ID użytkownika, którego dane mają zostać zaktualizowane", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nowe dane użytkownika do aktualizacji",
                    required = true
            )
            @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Usuń użytkownika",
            description = "Usuwa użytkownika na podstawie ID"
    )
    public void deleteUser(
            @Parameter(description = "ID użytkownika, który ma zostać usunięty", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
    }

}


