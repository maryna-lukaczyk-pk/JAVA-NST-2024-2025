package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) {
        // Sprawdzenie, czy użytkownik istnieje
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setUsername(user.getUsername());
            // Możesz dodać inne pola do zaktualizowania, jeśli to konieczne
            return userRepository.save(updatedUser);
        }
        // Jeśli użytkownik nie istnieje, można zwrócić null lub wykonać inną akcję
        return null;
    }

    public void deleteUser(Long id) {
        // Sprawdzamy, czy użytkownik istnieje przed usunięciem
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}
