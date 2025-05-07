package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(
        ProjectRepository projectRepository
    ) {
        this.projectRepository = projectRepository;
    }

    public Project getProject(
        Long id
    ) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> getAllProjects()
    {
        return projectRepository.findAll();
    }

    public Project createProject(
        Project project
    ) {
        return projectRepository.save(project);
    }

    public Project updateProject(
        Project projectData
    ) {
        Project project = projectRepository.findById(projectData.getId()).orElse(null);

        if (project != null) {
            BeanUtils.copyProperties(projectData, project);
            projectRepository.save(project);
        }

        return project;
    }

    public Project deleteProject(
        Long id
    ) {
        Project project = projectRepository.findById(id).orElse(null);

        if (project != null) {
            projectRepository.delete(project);
        }

        return project;
    }
}
