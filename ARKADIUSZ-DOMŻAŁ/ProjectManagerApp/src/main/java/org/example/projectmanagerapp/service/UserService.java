package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Users;
import org.springframework.stereotype.Service;
import org.example.projectmanagerapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<Users> getAllUsers(){
        return userRepository.findAll();
    }

    public Users createUser(Users user){
        return userRepository.save(user);
    }

    public Optional<Users> updateUser(Long id, Users updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(updatedUser.getUsername());
            return userRepository.save(user);
        });
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<Projects> getProjectsForUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> new ArrayList<>(user.getProjects()))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

}
