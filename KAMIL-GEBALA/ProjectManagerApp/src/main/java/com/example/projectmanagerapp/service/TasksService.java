package com.example.projectmanagerapp.service;
import com.example.projectmanagerapp.entity.Tasks;
import com.example.projectmanagerapp.repository.TasksRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TasksService {

    private final TasksRepository tasksRepository;

    public TasksService(TasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public List<Tasks> getAllTasks() {
        return tasksRepository.findAll();
    }

    public Tasks createTask(Tasks task) {
        return tasksRepository.save(task);
    }

    public Tasks updateTask(long id, Tasks task) {
        if(tasksRepository.existsById(id)){
            return tasksRepository.save(task);
        }
        return null;
    }

    public void deleteTask(long id) {tasksRepository.deleteById(id); }


}