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

    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    public void deleteTaskById(Integer id) {
        taskRepository.deleteById(id);
    }

    public Task updateTask(Integer id, Task taskDetails) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()) {
            Task existingTask = task.get();
            existingTask.setTitle(taskDetails.getTitle());
            return taskRepository.save(existingTask);
        }
        return null;
    }
}