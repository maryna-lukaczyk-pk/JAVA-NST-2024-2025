package s.kopec.ProjectManagerApp.controller;

import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.entity.Task;
import s.kopec.ProjectManagerApp.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("")
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Task> getById(@PathVariable Long id) {
        return taskRepository.findById(id);
    }

    @PostMapping("/create")
    public Task create(@RequestBody Task task) {
        return taskRepository.save(task);
    }

}
