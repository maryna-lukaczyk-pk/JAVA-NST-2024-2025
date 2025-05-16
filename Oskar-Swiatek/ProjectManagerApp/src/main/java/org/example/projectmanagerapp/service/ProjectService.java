package org.example.projectmanagerapp.service;


import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setName(projectDetails.getName());
        return projectRepository.save(project);
    }

    public void assignUserToProject(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userService.getUserById(userId);

        project.getAssignedUsers().add(user);
        projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        if (projectRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
