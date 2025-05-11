package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    //pobiera ID i caly obiekt Project z nowymi danymi
    public Project updateProject(Long id, Project updatedProject) {
        Project existing = projectRepository.findById(id).orElse(null);
        if (existing == null) return null;
        //do existing pobiera wartosci aktualne i zastępuje nowymi
        existing.setName(updatedProject.getName());
        existing.setTasks(updatedProject.getTasks());
        existing.setProjectUsers(updatedProject.getProjectUsers());
        return projectRepository.save(existing);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
}
//