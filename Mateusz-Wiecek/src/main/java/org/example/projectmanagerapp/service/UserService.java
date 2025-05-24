package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public List<User> getAllUsers() { return repo.findAll(); }
    public Optional<User> getUserById(Long id) { return repo.findById(id); }
    public User createUser(User u) { return repo.save(u); }

    public Optional<User> updateUser(Long id, User data) {
        return repo.findById(id).map(u -> {
            u.setUsername(data.getUsername());
            return repo.save(u);
        });
    }

    public boolean deleteUser(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
