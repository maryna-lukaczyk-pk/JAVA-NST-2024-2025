package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.ProjectUser;
import org.example.projectmanagerapp.repository.ProjectUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserService(ProjectUserRepository projectUserRepository) {
        this.projectUserRepository = projectUserRepository;
    }

    public List<ProjectUser> getAllProjectUsers() {
        return projectUserRepository.findAll();
    }

    public ProjectUser createProjectUser(ProjectUser projectUser) {
        return projectUserRepository.save(projectUser);
    }

    public ProjectUser updateProjectUser(Long id, ProjectUser projectUser) {
        Optional<ProjectUser> existingProjectUser = projectUserRepository.findById(id);
        if (existingProjectUser.isPresent()) {
            ProjectUser updatedProjectUser = existingProjectUser.get();
            updatedProjectUser.setProject(projectUser.getProject());
            updatedProjectUser.setUser(projectUser.getUser());
            // Zaktualizuj inne pola, jeśli to konieczne
            return projectUserRepository.save(updatedProjectUser);
        }
        return null;  // lub rzucić wyjątek, np. EntityNotFoundException
    }

    public void deleteProjectUser(Long id) {
        if (projectUserRepository.existsById(id)) {
            projectUserRepository.deleteById(id);
        }
    }
}
