package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepo) {
        this.userRepository = userRepo;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public User updateUser(long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        existingUser.setUsername(updatedUser.getUsername());
        return userRepository.save(existingUser);
    }

    public User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
