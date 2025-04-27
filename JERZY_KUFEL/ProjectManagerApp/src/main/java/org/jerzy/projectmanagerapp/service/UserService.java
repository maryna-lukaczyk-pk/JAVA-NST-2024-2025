package org.jerzy.projectmanagerapp.service;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.User;
import org.jerzy.projectmanagerapp.repository.UserRepository;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository repository) {
        this.userRepository = repository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User create(User user) {
        return this.userRepository.save(user);
    }
}
