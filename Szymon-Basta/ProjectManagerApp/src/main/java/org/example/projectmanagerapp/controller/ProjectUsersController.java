package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.ProjectUsers;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.ProjectUsersRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project-users")
public class ProjectUsersController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUsersRepository projectUsersRepository;

    public ProjectUsersController(ProjectRepository projectRepository,
                                  UserRepository userRepository,
                                  ProjectUsersRepository projectUsersRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectUsersRepository = projectUsersRepository;
    }

    @PostMapping
    public ResponseEntity<ProjectUsers> assignUserToProject(
            @RequestParam Long userId,
            @RequestParam Long projectId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Projects project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        ProjectUsers relation = new ProjectUsers();
        relation.setUser(user);
        relation.setProject(project);

        return ResponseEntity.ok(projectUsersRepository.save(relation));
    }
}
