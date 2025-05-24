package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository repo;
    public ProjectService(ProjectRepository repo) { this.repo = repo; }

    public List<Project> getAllProjects() { return repo.findAll(); }
    public Optional<Project> getProjectById(Long id) { return repo.findById(id); }
    public Project createProject(Project p) { return repo.save(p); }

    public Optional<Project> updateProject(Long id, Project data) {
        return repo.findById(id).map(p -> {
            p.setName(data.getName());
            return repo.save(p);
        });
    }

    public boolean deleteProject(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

}
