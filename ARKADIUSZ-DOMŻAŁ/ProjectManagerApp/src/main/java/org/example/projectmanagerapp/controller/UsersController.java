package org.example.projectmanagerapp.controller;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/Users")
public class UsersController {

    private final UserRepository userRepository;
    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        Users savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}
