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

    // Wyszukiwanie po ID
    public ProjectUser getProjectUserById(Long id) {
        Optional<ProjectUser> projectUser = projectUserRepository.findById(id);
        return projectUser.orElse(null);  // Można rzucić wyjątek, jeśli nie znaleziono
    }

    // Aktualizacja po ID
    public ProjectUser updateProjectUser(Long id, ProjectUser projectUser) {
        Optional<ProjectUser> existingProjectUser = projectUserRepository.findById(id);
        if (existingProjectUser.isPresent()) {
            ProjectUser updatedProjectUser = existingProjectUser.get();
            updatedProjectUser.setProject(projectUser.getProject());
            updatedProjectUser.setUser(projectUser.getUser());
            // Zaktualizuj inne pola, jeśli to konieczne
            return projectUserRepository.save(updatedProjectUser);
        }
        return null;  // Można rzucić wyjątek
    }

    // Usuwanie po ID
    public void deleteProjectUser(Long id) {
        if (projectUserRepository.existsById(id)) {
            projectUserRepository.deleteById(id);
        }
    }
}
