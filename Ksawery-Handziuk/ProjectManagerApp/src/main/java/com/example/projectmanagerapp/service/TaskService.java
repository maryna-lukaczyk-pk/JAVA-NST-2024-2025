// src/main/java/com/example/projectmanagerapp/service/TaskService.java
package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.entity.priority.HighPriority; // Załóżmy, że te klasy istnieją i są poprawne
import com.example.projectmanagerapp.entity.priority.LowPriority;
import com.example.projectmanagerapp.entity.priority.MediumPriority;
import com.example.projectmanagerapp.repository.TaskRepository;
import com.example.projectmanagerapp.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task setTaskPriority(Long taskId, String priorityLevel) {
        // Najpierw znajdź zadanie. Jeśli nie istnieje, rzuć wyjątek.
        // W tym miejscu można by dodać logikę tworzenia zadania, jeśli nie istnieje,
        // ale zgodnie z obecną logiką kontrolera, zadanie powinno już istnieć.
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Nie znaleziono zadania o ID: " + taskId));

        switch (priorityLevel.toUpperCase()) {
            case "HIGH" -> task.setPriority(new HighPriority());
            case "MEDIUM" -> task.setPriority(new MediumPriority());
            case "LOW" -> task.setPriority(new LowPriority());
            default -> {
                // Można rzucić wyjątek dla nieprawidłowego poziomu lub ustawić domyślny/null.
                throw new IllegalArgumentException("Nieprawidłowy poziom priorytetu: " + priorityLevel +
                        ". Dozwolone wartości to HIGH, MEDIUM, LOW.");
                // Alternatywnie, można ustawić priorytet na null lub zachować poprzedni:
                // task.setPriority(null);
            }
        }
        // Ważne: Jeśli pole `priority` w encji `Task` jest oznaczone jako `@Transient`,
        // zmiana ta nie zostanie zapisana w bazie danych przez `taskRepository.save(task)`.
        // Aby priorytet był persystowany, należy zmodyfikować encję `Task` tak, aby
        // mapowała priorytet na kolumnę w bazie (np. przez dodatkowe pole String i logikę konwersji,
        // lub używając enuma z @Enumerated).
        return taskRepository.save(task); // Repozytorium zapisuje zaktualizowany task (ale nie @Transient pole)
    }

    // Inne metody serwisu dla Task...
    // Np. CRUD dla zadań, jeśli potrzebne.
    // public Task createTask(String description) { ... }
    // public Task getTaskById(Long id) { ... }
    // public List<Task> getAllTasks() { ... }
    // public void deleteTask(Long id) { ... }
}