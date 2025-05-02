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
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                             .orElseThrow(() -> new RuntimeException("Task does not exist"));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTaskById(Long id, Task task) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task with id " + id + " does not exist");
        }

        task.setId(id);
        return taskRepository.save(task);
    }

    public Boolean deleteTaskById(Long id) {
        if (!taskRepository.existsById(id)) {
            return false;
        }

        taskRepository.deleteById(id);
        return true;
    }
}
