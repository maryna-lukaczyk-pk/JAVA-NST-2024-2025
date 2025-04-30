package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        // Sprawdzamy, czy zadanie istnieje
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task updatedTask = existingTask.get();
            updatedTask.setTitle(task.getTitle());
            updatedTask.setDescription(task.getDescription());
            updatedTask.setTaskType(task.getTaskType());
            updatedTask.setPriority(task.getPriority());
            updatedTask.setProject(task.getProject());
            // Możesz dodać inne pola do zaktualizowania, jeśli to konieczne
            return taskRepository.save(updatedTask);
        }
        // Jeśli zadanie nie istnieje, można zwrócić null lub wykonać inną akcję
        return null;
    }

    public void deleteTask(Long id) {
        // Sprawdzamy, czy zadanie istnieje przed usunięciem
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
    }
}
