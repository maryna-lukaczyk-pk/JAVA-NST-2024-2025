package s.kopec.ProjectManagerApp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;
import s.kopec.ProjectManagerApp.repository.TaskRepository;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for mapping projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("")
    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects from the database")
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a project by ID", description = "Returns a project by its ID")
    public Optional<Project> getById(
            @Parameter(description = "ID of the project to retrieve", required = true)
            @PathVariable Long id) {
        return projectRepository.findById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new project", description = "Adds a new project to the database")
    public Project create(
            @Parameter(description = "Project object to create", required = true)
            @RequestBody Project project) {
        return projectRepository.save(project);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a project by ID", description = "Deletes a project from the database using its ID")
    public void deleteById(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Long id) {
        projectRepository.deleteById(id);
    }

    @PutMapping("/update/{id}/{newName}")
    @Operation(summary = "Update project name", description = "Updates the name of an existing project")
    public void update(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New name for the project", required = true)
            @PathVariable String newName) {
        projectRepository.existsById(id);
        Project myProject = projectRepository.getReferenceById(id);
        myProject.setName(newName);
        projectRepository.save(myProject);
    }

}
