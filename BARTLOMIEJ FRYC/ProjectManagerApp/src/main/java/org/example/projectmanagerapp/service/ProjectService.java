package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            Project existingProject = project.get();
            // Update the project fields with the new details
            if (projectDetails.getName() != null) {
                existingProject.setName(projectDetails.getName());
            }
            if (projectDetails.getTasks() != null) {
                existingProject.setTasks(projectDetails.getTasks());
            }
            if (projectDetails.getUsers() != null) {
                existingProject.setUsers(projectDetails.getUsers());
            }

            return projectRepository.save(existingProject);
        }
        return null; // Or throw an exception
    }

    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
