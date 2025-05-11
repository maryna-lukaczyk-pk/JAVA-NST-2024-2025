package org.jerzy.projectmanagerapp.service;

import java.util.List;

import org.jerzy.projectmanagerapp.entity.Task;
import org.jerzy.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private final TaskRepository taskRepository;

  public TaskService(TaskRepository repository) {
    this.taskRepository = repository;
  }

  public List<Task> getAllTasks() {
    return this.taskRepository.findAll();
  }

  public Task getById(String id) {
    int taskId = 0;
    try {
      taskId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid id: " + e);
    }
    return this.taskRepository.findById(taskId).get();
  }

  public Task create(Task task) {
    return this.taskRepository.save(task);
  }

  public Task update(String id, Task task) {
    int taskId;
    try {
      taskId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid project ID: " + id);
    }

    return this.taskRepository.findById(taskId).map(existingTask -> {
      existingTask.setTitle(task.getTitle());
      existingTask.setDescription(task.getDescription());
      existingTask.setProject(task.getProject());
      existingTask.setPriority(task.getPriority());
      existingTask.setTask_type(task.getTask_type());
      return this.taskRepository.save(existingTask);
    }).orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
  }

  public void delete(String id) {
    int projectId;
    try {
      projectId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid project ID: " + id);
    }

    if (!this.taskRepository.existsById(projectId)) {
      throw new IllegalArgumentException("Project not found with ID: " + id);
    }

    this.taskRepository.deleteById(projectId);
  }
}
