package s.kopec.ProjectManagerApp.service;

import org.springframework.stereotype.Service;
import s.kopec.ProjectManagerApp.entity.Task;
import s.kopec.ProjectManagerApp.repository.TaskRepository;

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

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    public void updateTaskTitle(Long id, String newTaskTitle) {
        if(taskRepository.existsById(id)) {
            Task myTask = taskRepository.getReferenceById(id);
            myTask.setTitle(newTaskTitle);
            taskRepository.save(myTask);
        }
    }
}
