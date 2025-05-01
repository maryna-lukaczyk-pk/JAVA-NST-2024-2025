package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repozytorium.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class TaskService {

    private final TasksRepository tasksRepository;

    @Autowired
    public TaskService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    // Pobieranie zadania na podstawie ID
    public Tasks getTaskById(Long id) {
        Optional<Tasks> task = tasksRepository.findById(id);
        return task.orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    // Tworzenie zadania
    public Tasks createTask(Tasks task) {
        // Generowanie priorytetu przed zapisaniem
        task.generatePriority();
        return tasksRepository.save(task);
    }

    // Aktualizacja zadania na podstawie ID
    public Tasks updateTask(Long id, Tasks taskDetails) {
        return tasksRepository.findById(id)
                .map(task -> {
                    task.setTitle(taskDetails.getTitle());
                    task.setDescription(taskDetails.getDescription());
                    task.setTask_type(taskDetails.getTask_type()); // Dodanie aktualizacji priorytetu
                    // Można tu dodać logikę dla innych pól, jeśli będą dostępne
                    return tasksRepository.save(task);
                })
                .orElseThrow(() -> new NoSuchElementException("Task not found with id: " + id));
    }

    // Usuwanie zadania
    public void deleteTask(Long id) {
        if (tasksRepository.existsById(id)) {
            tasksRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Task not found with id: " + id);
        }
    }
}
