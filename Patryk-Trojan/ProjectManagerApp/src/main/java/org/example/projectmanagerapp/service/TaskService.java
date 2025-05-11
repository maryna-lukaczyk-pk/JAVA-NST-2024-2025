package org.example.projectmanagerapp.service;

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

    public List<Task> getAllTasks() {
        return this.taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return this.taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskRepository.deleteById(id);
    }

    public Task findTaskById(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    public void updateTask(Long id, Task task) {
        Task taskToUpdate = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setTitle(task.getTitle());
        taskToUpdate.setDescription(task.getDescription());

        this.taskRepository.save(taskToUpdate);

    }
}
