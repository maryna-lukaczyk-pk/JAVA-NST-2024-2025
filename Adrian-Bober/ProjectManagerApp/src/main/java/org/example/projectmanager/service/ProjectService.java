package org.example.projectmanager.service;

import org.example.projectmanager.entity.Project;
import org.example.projectmanager.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllUsers() {
        return projectRepository.findAll();
    }

    public Project createUser(Project project) {
        return projectRepository.save(project);
    }
}
