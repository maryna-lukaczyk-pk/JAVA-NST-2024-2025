package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> getByID(Long id) {
        return projectRepository.findById(id);
    }

    public Project create(Project newProject) {
        return projectRepository.save(newProject);
    }
}
