package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(long id,Project project) {
        if(projectRepository.existsById(id)){
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(long id) {projectRepository.deleteById(id); }
}