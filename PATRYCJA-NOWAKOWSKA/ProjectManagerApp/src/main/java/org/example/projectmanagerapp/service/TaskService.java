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
                .orElseThrow(() -> new RuntimeException("Zadanie o id:"  + id + " nie zostało znalezione"));
    }
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = getTaskById(id);
        task.setName(updatedTask.getName());
        task.setTaskType(updatedTask.getTaskType());
        task.setProject(updatedTask.getProject());
        task.setAssignedUser(updatedTask.getAssignedUser());
        task.setPriorityLevel(updatedTask.getPriorityLevel());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if(!taskRepository.existsById(id)) {
            throw new RuntimeException("Zadanie o ID " + id + " nie istnieje");
        }
        taskRepository.deleteById(id);
        }
    }

