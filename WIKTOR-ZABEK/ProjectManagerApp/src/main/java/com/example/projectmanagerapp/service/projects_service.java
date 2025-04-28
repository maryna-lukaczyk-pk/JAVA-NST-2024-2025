package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.projects;
import com.example.projectmanagerapp.repository.projects_repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public projects update_project(Long id, projects updatedProject) {
        if (projects_repository.existsById(id)) {
            updatedProject.setId(id);
            return projects_repository.save(updatedProject);
        }
        return null;
    }

    public void delete_project(Long id) {
        projects_repository.deleteById(id);
    }
}