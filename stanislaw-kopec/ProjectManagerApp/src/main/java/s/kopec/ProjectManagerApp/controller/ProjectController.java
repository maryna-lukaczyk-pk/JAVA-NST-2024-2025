package s.kopec.ProjectManagerApp.controller;


import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;
import s.kopec.ProjectManagerApp.repository.TaskRepository;

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

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        projectRepository.deleteById(id);
    }

    @PutMapping("/update/{id}/{newName}")
    public void update(@PathVariable Long id, @PathVariable String newName) {
        projectRepository.existsById(id);
        Project myProject = projectRepository.getReferenceById(id);
        myProject.setName(newName);
    }

}
