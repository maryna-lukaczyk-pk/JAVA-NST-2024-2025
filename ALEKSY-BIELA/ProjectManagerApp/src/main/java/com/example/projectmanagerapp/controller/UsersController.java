package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import com.example.projectmanagerapp.repozytorium.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    // Get project by id
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Optional<Users> users = usersRepository.findById(id);
        return users.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new project
    @PostMapping
    public ResponseEntity<Users> createProject(@RequestBody Users users) {
        Users createdTasks = usersRepository.save(users);
        return new ResponseEntity<>(createdTasks, HttpStatus.CREATED);
    }
}
