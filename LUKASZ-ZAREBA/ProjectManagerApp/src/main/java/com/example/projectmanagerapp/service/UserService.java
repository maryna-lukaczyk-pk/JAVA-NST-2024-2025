package com.example.projectmanagerapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(int id, User user) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id " + id);
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public void delete(int id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }
}
