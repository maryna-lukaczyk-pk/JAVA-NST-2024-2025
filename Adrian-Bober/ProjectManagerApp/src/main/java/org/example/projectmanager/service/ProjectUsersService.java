package org.example.projectmanager.service;

import org.example.projectmanager.entity.Project;
import org.example.projectmanager.entity.ProjectUsers;
import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.ProjectUsersRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectUsersService {
    private final ProjectUsersRepository projectUsersRepository;

    public ProjectUsersService(ProjectUsersRepository projectUsersRepository) {
        this.projectUsersRepository = projectUsersRepository;
    }

    public ProjectUsers createProjectUser(Project project, Users user) {
        ProjectUsers projectUser = new ProjectUsers();
        projectUser.setProject(project);
        projectUser.setUser(user);
        return projectUsersRepository.save(projectUser);
    }

    public List<Users> getUsersByProjectId(Long projectId) {
        return projectUsersRepository.findByProjectId(projectId)
                .stream()
                .map(ProjectUsers::getUser)
                .collect(Collectors.toList());
    }

    public List<Project> getProjectsByUserId(Long userId) {
        return projectUsersRepository.findByUserId(userId)
                .stream()
                .map(ProjectUsers::getProject)
                .collect(Collectors.toList());
    }
}