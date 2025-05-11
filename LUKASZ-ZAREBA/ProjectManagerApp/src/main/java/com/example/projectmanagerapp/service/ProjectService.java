package com.example.projectmanagerapp.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project getById(int id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id " + id));
    }

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public Project update(int id, Project project) {
        if (!projectRepository.existsById(id)) {
            throw new NoSuchElementException("Project not found with id " + id);
        }
        project.setId(id);
        return projectRepository.save(project);
    }

    public void delete(int id) {
        if (!projectRepository.existsById(id)) {
            throw new NoSuchElementException("Project not found with id " + id);
        }
        projectRepository.deleteById(id);
    }
}
