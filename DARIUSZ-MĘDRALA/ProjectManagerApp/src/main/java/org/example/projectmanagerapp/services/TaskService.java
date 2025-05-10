package org.example.projectmanagerapp.services;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
        return task;
    }

    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
