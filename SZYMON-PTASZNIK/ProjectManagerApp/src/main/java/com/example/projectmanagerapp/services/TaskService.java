package com.example.projectmanagerapp.services;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepo) {this.taskRepository = taskRepo;}

    public List<Task> getAllTasks() {return taskRepository.findAll();}

    public Task createTask(Task task) {return taskRepository.save(task);}

    public void deleteTask(long taskId) {taskRepository.deleteById(taskId);}

    public Task updateTask(long taskId, Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId).orElseThrow(()->new RuntimeException("Task not found"));
        existingTask.setProject(updatedTask.getProject());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setTaskType(updatedTask.getTaskType());
        existingTask.setTitle(updatedTask.getTitle());
        return taskRepository.save(existingTask);
    }

    public Task getTaskById(long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }
}
