package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.TaskDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.priority.PriorityLevel;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Serwis zarządzający operacjami na encji zadania
@Service
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private TaskDTO toDTO(Task t) {
        return new TaskDTO( t.getId(), t.getTitle(), t.getDescription(), t.getTaskType().name(), t.getProject().getId(), t.getPriority());
    }

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskDTO createTask(Task task) {
        Long projId = task.getProject().getId();
        Project proj = projectRepository.findById(projId).orElseThrow(()
                -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Project not found with ID: " + projId));
        task.setProject(proj);

        if (task.getPriorityLevel() == null) { task.setPriorityLevel(PriorityLevel.UNDEFINED); }

        Task saved = taskRepository.save(task);
        return toDTO(saved);
    }

    public List<TaskDTO> getAllTasks() { return taskRepository.findAll().stream().map(this::toDTO).toList(); }

    public TaskDTO getTaskById(Long id) {
        Task t = taskRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Task not found with ID: " + id));
        return toDTO(t);
    }

    public TaskDTO updateTask(Long id, Task updatedTask) {
        Task existing = taskRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with ID: " + id));

        if (updatedTask.getTitle() != null) { existing.setTitle(updatedTask.getTitle()); }

        if (updatedTask.getDescription() != null) { existing.setDescription(updatedTask.getDescription()); }

        if (updatedTask.getTaskType() != null) { existing.setTaskType(updatedTask.getTaskType()); }

        if (updatedTask.getPriorityLevel() != null && updatedTask.getPriorityLevel() != PriorityLevel.UNDEFINED) {
            existing.setPriorityLevel(updatedTask.getPriorityLevel());
        }

        if (updatedTask.getProject() != null && updatedTask.getProject().getId() != null) {
            Long newProjId = updatedTask.getProject().getId();
            Project newProj = projectRepository.findById(newProjId).orElseThrow(()
                    -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with ID: " + newProjId));
            existing.setProject(newProj);
        }

        Task saved = taskRepository.save(existing);
        return toDTO(saved);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);
    }
}