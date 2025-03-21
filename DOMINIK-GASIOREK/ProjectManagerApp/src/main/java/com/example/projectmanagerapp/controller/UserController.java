package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    private UserRepository userRepository;

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping
    public Optional<User> getByID(@PathVariable Long id) {
        return userRepository.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }
}