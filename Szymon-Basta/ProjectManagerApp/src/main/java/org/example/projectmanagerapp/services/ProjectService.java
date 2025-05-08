package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    public Projects createProject(Projects project) {
        return projectRepository.save(project);
    }
}
