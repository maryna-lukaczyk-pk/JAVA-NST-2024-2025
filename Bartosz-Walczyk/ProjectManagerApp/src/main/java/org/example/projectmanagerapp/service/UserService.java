package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(
        UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    public User getUserById(
        Long id
    ) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User createUser(
        User user
    ) {
        return userRepository.save(user);
    }

    public User updateUser(
        User userData
    ) {
        User user = userRepository.findById(userData.getId()).orElse(null);

        if (user != null) {
            BeanUtils.copyProperties(userData, user);
            userRepository.save(user);
        }

        return user;
    }

    public User deleteUser(
        Long id
    ) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            userRepository.delete(user);
        }

        return user;
    }
}
