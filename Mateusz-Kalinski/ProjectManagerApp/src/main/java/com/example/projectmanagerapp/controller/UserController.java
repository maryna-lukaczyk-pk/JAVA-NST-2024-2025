package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name="test", description="test")
public class UserController {

    private UserRepository userRepository;

    @Operation(summary = "test", description = "test")
    @GetMapping("/all")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation (summary = "Tworzy uzytkownika", description = "Tworzy nowego uzytkownika")
    @PostMapping("/create")
    public Users createUser(@RequestBody Users user) {
        return userRepository.save(user);
    }

    @Operation (summary = "test", description = "test")
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Operation (summary = "test", description = "test")
    @DeleteMapping("/delete{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @Operation (summary = "test", description = "test")
    @PutMapping("/update{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users user) {
        return userRepository.save(user);
    }
}