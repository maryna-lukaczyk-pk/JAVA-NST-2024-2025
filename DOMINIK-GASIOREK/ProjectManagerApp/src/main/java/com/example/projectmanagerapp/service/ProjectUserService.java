package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.ProjectUserRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectUserService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    public ProjectUser addUserToProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);

        ProjectUser pu = new ProjectUser();
        pu.setUser(user);
        pu.setProject(project);
        return projectUserRepository.save(pu);
    }
}