package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task updateTask(Long id, Task taskDetails) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            Task existingTask = task.get();
            // Update the task fields with the new details
            // You might want to add more fields here depending on your requirements
            if (taskDetails.getTitle() != null) {
                existingTask.setTitle(taskDetails.getTitle());
            }
            if (taskDetails.getDescription() != null) {
                existingTask.setDescription(taskDetails.getDescription());
            }
            if (taskDetails.getTaskType() != null) {
                existingTask.setTaskType(taskDetails.getTaskType());
            }
            if (taskDetails.getProject() != null) {
                existingTask.setProject(taskDetails.getProject());
            }
            if (taskDetails.getPriorityName() != null) {
                existingTask.setPriorityName(taskDetails.getPriorityName());
                existingTask.loadPriorityFromName();
            }

            return taskRepository.save(existingTask);
        }
        return null; // Or throw an exception
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
