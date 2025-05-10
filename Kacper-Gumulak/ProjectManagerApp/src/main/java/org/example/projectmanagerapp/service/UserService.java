package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.UserDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Serwis zarządzający operacjami na encji użytkownicy
@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    public UserService(UserRepository userRepository, ProjectRepository projectRepository, ProjectUserRepository projectUserRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.projectUserRepository = projectUserRepository;
    }

    private UserDTO toDTO(User u) {
        var projectId = u.getProjectUsers().stream().map(pu -> pu.getProject().getId()).toList();
        return new UserDTO(u.getId(), u.getUsername(), projectId);
    }

    public UserDTO createUser(User user) {
        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public List<UserDTO> getAllUsers() { return userRepository.findAll().stream().map(this::toDTO).toList(); }

    public UserDTO getUserById(Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id));
        return toDTO(u);
    }

    public UserDTO updateUser(Long id, User updatedUser) {
        User existing = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id));
        existing.setUsername(updatedUser.getUsername());
        User saved = userRepository.save(existing);
        return toDTO(saved);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserDTO assignUserToProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));
        Project project = projectRepository.findById(projectId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with ID: " + projectId));

        ProjectUser pu = new ProjectUser(project, user);
        project.getProjectUsers().add(pu);
        user.getProjectUsers().add(pu);
        projectUserRepository.save(pu);

        return toDTO(user);
    }
}