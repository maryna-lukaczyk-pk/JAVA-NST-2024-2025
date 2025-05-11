package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Projects;
import com.example.projectmanagerapp.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
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

    public Projects updateProject(Long id, Projects newData){
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(newData.getName());
                    project.setTasks(newData.getTasks());
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
}