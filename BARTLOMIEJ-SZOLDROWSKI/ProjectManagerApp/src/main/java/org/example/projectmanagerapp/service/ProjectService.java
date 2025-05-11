package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // Aktualizujemy tylko możliwe do zmiany pola
        if (projectDetails.getName() != null) {
            project.setName(projectDetails.getName());
        }
        // Uwaga: Nie aktualizujemy bezpośrednio listy tasks ani users
        // aby uniknąć niekontrolowanych zmian w relacjach

        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        // Sprawdzamy czy projekt nie ma przypisanych zadań
        if (!project.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete project with assigned tasks");
        }

        // Usuwamy powiązania z użytkownikami
        project.getUsers().forEach(user -> user.getProjects().remove(project));

        projectRepository.delete(project);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }
}
