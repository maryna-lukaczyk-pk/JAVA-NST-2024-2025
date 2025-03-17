package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    List<User> all() {
        return userRepository.findAll();
    }

    @PostMapping("/")
    User newUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }
}
