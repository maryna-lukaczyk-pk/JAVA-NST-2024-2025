package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    public Projects getProjectById(long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    public Projects createProject(Projects project) {
        return projectRepository.save(project);
    }

    public Projects updateProject(Long id, Projects newData) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(newData.getName());
                    return projectRepository.save(project);
                })
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    public void assignUserToProject(Long projectId, Long userId) {
        Projects project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        project.getUsers().add(user);
        user.getProjects().add(project);

        userRepository.save(user);
    }
}
