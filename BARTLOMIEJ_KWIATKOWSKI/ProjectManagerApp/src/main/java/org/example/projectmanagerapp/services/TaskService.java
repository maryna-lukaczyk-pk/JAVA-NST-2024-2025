package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Tasks> getAllTasks() {
        return taskRepository.findAll();
    }

    public Tasks createTask(Tasks task) {
        return taskRepository.save(task);
    }

    public Tasks updateTask(Long id, Tasks updatedTask) {
        Optional<Tasks> existingTaskOptional = taskRepository.findById(id);

        if (existingTaskOptional.isPresent()) {
            Tasks existingTask = existingTaskOptional.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setTaskType(updatedTask.getTaskType());
            existingTask.setProject(updatedTask.getProject());
            return taskRepository.save(existingTask);
        }

        return null;
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    public Optional<Tasks> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

}