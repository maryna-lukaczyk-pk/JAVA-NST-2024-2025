package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;
import com.example.projectmanagerapp.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
// Usunięto import Optional, bo findById zwraca Optional, ale metody serwisu zwracają User lub rzucają wyjątek

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 9. Możliwość wyszukania obiektu po jego ID
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User createUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username '" + username + "' already exists.");
        }
        User newUser = new User(username);
        return userRepository.save(newUser);
    }

    // 9. Możliwość aktualizacji danych obiektu po jego ID
    @Transactional
    public User updateUser(Long id, String newUsername) { // Przyjmuje ID i nowe dane
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id + " for update."));

        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("New username cannot be empty.");
        }

        // Opcjonalnie: sprawdź, czy nowy username nie jest już zajęty przez INNEGO użytkownika
        if (!userToUpdate.getUsername().equals(newUsername) && userRepository.existsByUsername(newUsername)) {
            throw new DuplicateResourceException("Username '" + newUsername + "' is already taken by another user.");
        }

        userToUpdate.setUsername(newUsername);
        return userRepository.save(userToUpdate); // Zapisz zaktualizowanego użytkownika
    }

    // 9. Możliwość usunięcia obiektu po jego ID
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id + " for deletion.");
        }
        userRepository.deleteById(id);
    }
}