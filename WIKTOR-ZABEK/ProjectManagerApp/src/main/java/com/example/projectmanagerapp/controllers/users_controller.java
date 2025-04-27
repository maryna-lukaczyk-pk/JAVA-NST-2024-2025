package com.example.projectmanagerapp.controllers;

import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.repository.users_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class users_controller {

    @Autowired
    private users_repository users_repo;

    @GetMapping
    public List<users> getAllUsers() {
        return users_repo.findAll();
    }

    @PostMapping("/")
    public users createUser(@RequestBody users user) {
        return users_repo.save(user);
    }
}