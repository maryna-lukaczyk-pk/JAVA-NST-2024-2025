package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repo;
    public TaskService(TaskRepository repo) { this.repo = repo; }

    public List<Task> getAllTasks() { return repo.findAll(); }
    public Optional<Task> getTaskById(Long id) { return repo.findById(id); }
    public Task createTask(Task t) { return repo.save(t); }

    public Optional<Task> updateTask(Long id, Task data) {
        return repo.findById(id).map(t -> {
            t.setTitle(data.getTitle());
            t.setDescription(data.getDescription());
            t.setTaskType(data.getTaskType());
            t.setProject(data.getProject());
            return repo.save(t);
        });
    }

    public boolean deleteTask(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
