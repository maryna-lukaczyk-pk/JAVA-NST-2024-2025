package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.example.projectmanagerapp.schemas.TaskDTO;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    public Task findTaskById(Integer id) throws NotFoundException {
        return taskRepository
                .findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public void addTask(TaskDTO dto) {
        Optional<Project> optionalProject = projectRepository.findById(dto.getProjectId());

        if(optionalProject.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Project with id=" + dto.getProjectId() + " not found");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setTaskType(dto.getTaskType());
        task.setProject(optionalProject.get());

        taskRepository.save(task);
    }

    public void deleteTask(Integer id) throws NotFoundException {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new NotFoundException();
        }
    }

    public void updateTaskAttributes(TaskDTO dto, Integer id) throws NotFoundException {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()) {
            Task task = optionalTask.get();

            if(dto.getTitle() != null) {
                task.setTitle(dto.getTitle());
            }
            if(dto.getDescription() != null) {
                task.setDescription(dto.getDescription());
            }
            if(dto.getTaskType() != null) {
                task.setTaskType(dto.getTaskType());
            }

            taskRepository.save(task);
        } else {
            throw new NotFoundException();
        }
    }
}
