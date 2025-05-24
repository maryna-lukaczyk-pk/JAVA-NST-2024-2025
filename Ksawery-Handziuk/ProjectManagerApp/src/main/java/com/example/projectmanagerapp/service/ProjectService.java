// src/main/java/com/example/projectmanagerapp/service/ProjectService.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.repository.UserRepository;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;
import com.example.projectmanagerapp.exception.DuplicateResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository; // Potrzebne do operacji na użytkownikach w kontekście projektów

    @Autowired
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Project createProject(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa projektu nie może być pusta.");
        }
        // Sprawdzenie, czy projekt o takiej nazwie już istnieje (jeśli nazwy mają być unikalne)
        if (projectRepository.existsByName(name)) {
            throw new DuplicateResourceException("Projekt o nazwie '" + name + "' już istnieje.");
        }
        Project newProject = new Project(name);
        return projectRepository.save(newProject);
    }

    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono projektu o ID: " + id));
    }

    @Transactional
    public Project updateProject(Long id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nowa nazwa projektu nie może być pusta.");
        }
        Project projectToUpdate = getProjectById(id); // Używa metody getProjectById, która rzuca wyjątek jeśli nie znaleziono

        // Sprawdzenie, czy nowa nazwa nie jest już zajęta przez INNY projekt
        if (!projectToUpdate.getName().equals(newName) && projectRepository.existsByName(newName)) {
            throw new DuplicateResourceException("Nazwa projektu '" + newName + "' jest już zajęta.");
        }

        projectToUpdate.setName(newName);
        return projectRepository.save(projectToUpdate);
    }

    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nie znaleziono projektu o ID: " + id + " do usunięcia.");
        }
        // Dodatkowa logika: np. usuwanie powiązań z użytkownikami, jeśli kaskada nie jest ustawiona
        // lub jeśli chcemy ręcznie zarządzać tym procesem.
        // W tym przypadku, relacja ManyToMany z User może wymagać ręcznego czyszczenia
        // lub odpowiedniej konfiguracji kaskady (np. po stronie User, jeśli Project jest właścicielem).
        // Dla uproszczenia, zakładamy, że usunięcie projektu nie powoduje problemów z osieroconymi relacjami
        // lub że są one zarządzane przez JPA (np. tabela łącząca jest czyszczona).
        Project project = getProjectById(id);
        // Ręczne usunięcie powiązań z użytkownikami, aby uniknąć problemów z więzami integralności
        // lub aby poprawnie zaktualizować kolekcje po obu stronach relacji.
        // To jest ważne, jeśli nie ma kaskady usuwania skonfigurowanej w odpowiedni sposób.
        for (User user : project.getUsers()) {
            user.getProjects().remove(project);
        }
        project.getUsers().clear();
        projectRepository.save(project); // Zapisz zmiany w powiązaniach przed usunięciem projektu

        projectRepository.deleteById(id);
    }

    @Transactional
    public Project addUserToProject(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + userId));

        if (project.getUsers().contains(user)) {
            throw new IllegalStateException("Użytkownik o ID: " + userId + " jest już przypisany do projektu o ID: " + projectId);
        }

        project.addUser(user); // Metoda pomocnicza w encji Project zarządza obustronną relacją
        return projectRepository.save(project);
    }

    @Transactional
    public Project removeUserFromProject(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono użytkownika o ID: " + userId));

        if (!project.getUsers().contains(user)) {
            throw new ResourceNotFoundException("Użytkownik o ID: " + userId + " nie jest przypisany do projektu o ID: " + projectId);
        }

        project.removeUser(user); // Metoda pomocnicza w encji Project
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Set<User> getUsersFromProject(Long projectId) {
        Project project = getProjectById(projectId);
        // Aby uniknąć problemu LazyInitializationException, upewnij się, że kolekcja jest zainicjowana.
        // Można to zrobić przez явne wywołanie getSize() lub przez użycie JOIN FETCH w zapytaniu repozytorium.
        // W tym przypadku, standardowe findById i dostęp do getUsers() w ramach transakcji powinno wystarczyć.
        // Hibernate.initialize(project.getUsers()); // Jedna z opcji, jeśli jest problem z lazy loading
        return project.getUsers();
    }
}