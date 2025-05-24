package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public UserService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
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

    public User updateUserById(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with id " + id + " does not exist");
        }

        user.setId(id);
        return userRepository.save(user);
    }

    public Boolean deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            return false;
        }

        var user = userRepository.findById(id)
                                 .orElseThrow(() -> new RuntimeException("User with id " + id + " does not exist"));

        var projects = projectRepository.findByUsers_Id(id);
        for (var project : projects) {
            project.getUsers().remove(user);
            projectRepository.save(project);
        }

        userRepository.deleteById(id);
        return true;
    }
}
