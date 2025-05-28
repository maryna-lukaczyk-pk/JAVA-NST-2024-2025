package org.example.projectmanagerapp.services;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repositories.ProjectRepository;
import org.example.projectmanagerapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    public Project assignUsersToProject(int projectId, List<Integer> userIds) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Set<User> users = new HashSet<>(userRepository.findAllById(userIds));
        project.setUsers(users);
        return projectRepository.save(project);

    }

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(int id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(int id) {
        projectRepository.deleteById(id);
    }
}