// ProjectService.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }



    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project project) {
        Optional<Project> existing = projectRepository.findById(id);
        if (existing.isPresent()) {
            project.setId(id);
            return projectRepository.save(project);
        } else {
            throw new RuntimeException("Project not found with id " + id);
        }
    }
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    @Transactional
    public void assignUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        project.getUsers().add(user);
        projectRepository.save(project);
    }


    public List<User> getUsersAssignedToProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return new ArrayList<>(project.getUsers());
    }


}



