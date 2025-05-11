package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.repository.projects_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class projects_service {

    @Autowired
    private projects_repository projects_repository;

    public List<projects> getAllProjects() {
        return projects_repository.findAll();
    }

    public projects create_project(projects project) {
        return projects_repository.save(project);
    }

    // Find project by ID
    public Optional<projects> getProjectById(Long id) {
        return projects_repository.findById(id);
    }

    // Delete project by ID
    public void deleteProjectById(Long id) {
        projects_repository.deleteById(id);
    }

    // Update project by ID
    public projects updateProject(Long id, projects updatedProject) {
        Optional<projects> existingProject = projects_repository.findById(id);
        if (existingProject.isPresent()) {
            projects project = existingProject.get();
            project.setName(updatedProject.getName());
            return projects_repository.save(project);
        }
        return null; // Or throw an exception indicating project not found
    }
}