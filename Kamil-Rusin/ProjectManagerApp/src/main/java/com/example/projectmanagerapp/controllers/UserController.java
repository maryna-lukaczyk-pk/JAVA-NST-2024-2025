package com.example.projectmanagerapp.controllers;


import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    public final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }
}
