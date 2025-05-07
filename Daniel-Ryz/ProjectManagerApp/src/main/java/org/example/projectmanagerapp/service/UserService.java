package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CreateUserRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectService projectService;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public Users createUser(CreateUserRequest request) {
        Users user = new Users();
        user.setUsername(request.username());
        return userRepository.save(user);
    }

    @Transactional
    public Users updateUser(Long id, CreateUserRequest request) {
        Users user = getUserById(id);
        user.setUsername(request.username());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void assignProjectToUser(Long projectId, Long userId) {
        Users user = getUserById(userId);
        Project project = getProjectById(projectId);

        user.getProjects().add(project);
        userRepository.save(user);
    }

    private Project getProjectById(Long projectId) {
        return projectService.getProjectById(projectId);
    }
}
