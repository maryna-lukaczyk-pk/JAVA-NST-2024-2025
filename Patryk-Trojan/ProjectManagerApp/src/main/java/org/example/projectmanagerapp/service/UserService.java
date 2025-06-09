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
        return this.userRepository.findAll();
    }

    public User findUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        System.out.println("Saving user: " + user.getUsername());
        User saved = userRepository.save(user);
        System.out.println("Saved user with id: " + saved.getId());
        return saved;
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);

    }

    public void deleteAllUsers() {
        this.userRepository.deleteAll();
    }

    public void updateUser(Long id, User user) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userToUpdate.setUsername(user.getUsername());
        this.userRepository.save(userToUpdate);
    }
}
