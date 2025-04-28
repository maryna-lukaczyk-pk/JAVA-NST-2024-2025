package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.repository.users_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public users update_user(Long id, users updatedUser) {
        if (users_repository.existsById(id)) {
            updatedUser.setId(id);
            return users_repository.save(updatedUser);
        }
        return null;
    }

    public void delete_user(Long id) {
        users_repository.deleteById(id);
    }
}