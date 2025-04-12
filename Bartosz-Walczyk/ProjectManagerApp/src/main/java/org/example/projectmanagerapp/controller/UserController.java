package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController
{
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getUsers()
    {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
        @PathVariable Long id
    ) {
        return ResponseEntity.ok(userRepository.findById(id).orElse(null));
    }

    @PostMapping
    public ResponseEntity<User> createUser(
        @RequestBody User user
    ) {
        user.setId(null);
        User newUser = userRepository.save(user);

        return ResponseEntity.ok(newUser);
    }
}
