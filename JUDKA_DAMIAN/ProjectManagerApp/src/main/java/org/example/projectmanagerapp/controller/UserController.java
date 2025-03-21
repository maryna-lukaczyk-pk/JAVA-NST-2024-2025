package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.projectmanagerapp.entity.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);

        Map<String, String> response = new HashMap<>();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        response.put("success", "User created");
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            Map<String, String> response = new HashMap<>();
            response.put("success", "User deleted");
            return ResponseEntity.ok(response);
        } else {
          Map<String, String> errorResponse = new HashMap<>();
          errorResponse.put("error", "User not found");
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody User user, @PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();

        if(userRepository.existsById(id)) {
            user.setId(id);
            userRepository.save(user);
            response.put("success", "User updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
