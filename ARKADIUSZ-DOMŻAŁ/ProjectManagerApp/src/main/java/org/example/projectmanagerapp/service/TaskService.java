package org.example.projectmanagerapp.service;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.entity.Users;
import org.springframework.stereotype.Service;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    public List<Tasks> getAllTasks(){
        return taskRepository.findAll();
    }

    public Tasks createTask(Tasks task) {
        return taskRepository.save(task);
    }

    //metoda PUT
    public Optional<Tasks> updateTask(Long id, Tasks updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setTaskType(updatedTask.getTaskType());
            task.setProject(updatedTask.getProject());
            return taskRepository.save(task);
        });
    }

    //metoda DELETE
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Optional<Tasks> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

}
