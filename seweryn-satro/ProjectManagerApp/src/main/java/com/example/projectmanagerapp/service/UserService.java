package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public User newUser(User user) {
        return userRepository.save(user);
    }

    public User update(Integer id, User newUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setProjects(newUser.getProjects());
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    public void delete(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }

    public User userById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

}
