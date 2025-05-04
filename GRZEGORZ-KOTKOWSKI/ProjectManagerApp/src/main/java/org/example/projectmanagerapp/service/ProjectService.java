package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
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

    // --------------------------
// Metoda do aktualizacji
// --------------------------
    public Project updateProject(Long id, Project newProject) {
        // Wyszukujemy projekt po ID lub rzucamy wyjątek
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Project with id " + id + " not found."));

        // Ustawiamy nowe pola
        existingProject.setName(newProject.getName());
        // Jeśli zachodzi potrzeba, można ustawiać pozostałe pola
        // existingProject.setUsers(newProject.getUsers()); – w zależności od wymagań

        return projectRepository.save(existingProject);
    }

    // --------------------------
// Metoda do usuwania
// --------------------------
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException(
                    "Project with id " + id + " does not exist.");
        }
        projectRepository.deleteById(id);
    }
}