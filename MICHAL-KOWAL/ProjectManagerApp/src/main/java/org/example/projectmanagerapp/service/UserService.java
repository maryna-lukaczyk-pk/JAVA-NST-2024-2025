package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new RuntimeException("User does not exist"));
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateById(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with id " + id + " does not exist");
        }

        user.setId(id);
        return userRepository.save(user);
    }

    public Boolean deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        userRepository.deleteById(id);
        return true;
    }
}
