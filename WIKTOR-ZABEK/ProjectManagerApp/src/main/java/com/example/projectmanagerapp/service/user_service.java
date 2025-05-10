package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.repository.users_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class user_service {

    @Autowired
    private users_repository users_repository;

    public List<users> getAllUsers() {
        return users_repository.findAll();
    }

    public users create_user(users user) {
        return users_repository.save(user);
    }

    // Find user by ID
    public Optional<users> getUserById(Long id) {
        return users_repository.findById(id);
    }

    // Delete user by ID
    public void deleteUserById(Long id) {
        users_repository.deleteById(id);
    }

    // Update user by ID
    public users updateUser(Long id, users updatedUser) {
        Optional<users> existingUser = users_repository.findById(id);
        if (existingUser.isPresent()) {
            users user = existingUser.get();
            user.setUsername(updatedUser.getUsername());
            return users_repository.save(user);
        }
        return null; // Or throw an exception indicating user not found
    }
}