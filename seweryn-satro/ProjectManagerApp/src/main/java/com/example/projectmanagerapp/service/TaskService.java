package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Task;
import com.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> allTasks() {
        return taskRepository.findAll();
    }

    public Task newTask(Task task) {
        return taskRepository.save(task);
    }

    public Task update(Integer id, Task newTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(newTask.getTitle());
                    task.setDescription(newTask.getDescription());
                    task.setTask_type(newTask.getTask_type());
                    task.setProject(newTask.getProject());
                    task.setPriority(newTask.getPriority());
                    return taskRepository.save(task);
                })
                .orElse(null);
    }

    public void delete(Integer id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        }
    }
}
