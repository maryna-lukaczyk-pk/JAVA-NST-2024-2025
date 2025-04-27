package s.kopec.ProjectManagerApp.controller;


import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("")
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Project> getById(@PathVariable Long id) {
        return projectRepository.findById(id);
    }

    @PostMapping("/create")
    public Project create(@RequestBody Project project) {
        return projectRepository.save(project);
    }

}
