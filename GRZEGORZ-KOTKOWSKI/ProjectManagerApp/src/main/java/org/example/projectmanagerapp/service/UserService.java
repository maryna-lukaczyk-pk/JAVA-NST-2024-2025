package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    // --------------------------
// Metoda do aktualizacji
// --------------------------
    public Users updateUser(Long id, Users updatedUser) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "User with id " + id + " not found."));

        existingUser.setUsername(updatedUser.getUsername());
        // W razie potrzeby można ustawiać także np. existingUser.setProjects(...)

        return userRepository.save(existingUser);
    }

    // --------------------------
// Metoda do usuwania
// --------------------------
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException(
                    "User with id " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }
}
