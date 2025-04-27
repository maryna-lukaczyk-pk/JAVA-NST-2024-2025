package org.example.projectmanagerapp.controller;


import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // GET
    @GetMapping
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    // POST
    @PostMapping
    public Users createUser(
            @RequestBody Users user) {
        return userRepository.save(user);
    }
}
