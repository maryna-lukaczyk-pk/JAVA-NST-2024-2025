package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository; // Dodaj import
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository; // Dodaj pole dla UserRepository

    // Zaktualizuj konstruktor, aby wstrzykiwał UserRepository
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository; // Zainicjuj pole
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project updateProject(Long id, Project projectDetails) {
        if (projectRepository.existsById(id)) {
            projectDetails.setId(id); // Upewnij się, że ID jest ustawione dla aktualizowanego obiektu
            return projectRepository.save(projectDetails);
        }
        return null; // Lub rzuć wyjątek, np. ResourceNotFoundException
    }

    public Project addUserToProject(Long projectId, Long userId) {
        // Pobierz projekt lub rzuć wyjątek, jeśli nie istnieje
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId)); // Możesz rozważyć dedykowany wyjątek

        // Pobierz użytkownika lub rzuć wyjątek, jeśli nie istnieje
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); // Możesz rozważyć dedykowany wyjątek

        // Dodaj użytkownika do zbioru użytkowników projektu
        // Upewnij się, że kolekcja users w encji Project jest zainicjalizowana (np. w konstruktorze lub getterze)
        // jeśli używasz np. new HashSet<>() przy deklaracji pola.
        project.getUsers().add(user);

        // Opcjonalnie: jeśli relacja jest dwukierunkowa i zarządzana również przez Users
        // user.getProjects().add(project);

        // Zapisz zaktualizowany projekt
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) { // Dobrą praktyką jest sprawdzenie, czy encja istnieje przed usunięciem
            projectRepository.deleteById(id);
        } else {
            throw new RuntimeException("Project not found with id: " + id); // Lub obsłuż inaczej
        }
    }
}
