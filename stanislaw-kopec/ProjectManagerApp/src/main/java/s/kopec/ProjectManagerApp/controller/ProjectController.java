package s.kopec.ProjectManagerApp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.Project;
import s.kopec.ProjectManagerApp.repository.ProjectRepository;
import s.kopec.ProjectManagerApp.service.ProjectService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for mapping projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("")
    @Operation(summary = "Retrieve all projects", description = "Returns a list of all projects from the database")
    public List<Project> getAll() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a project by ID", description = "Returns a project by its ID")
    public Optional<Project> getById(
            @Parameter(description = "ID of the project to retrieve", required = true)
            @PathVariable Long id) {
        return projectService.findProjectById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new project", description = "Adds a new project to the database")
    public Project create(
            @Parameter(description = "Project object to create", required = true)
            @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a project by ID", description = "Deletes a project from the database using its ID")
    public void deleteById(
            @Parameter(description = "ID of the project to delete", required = true)
            @PathVariable Long id) {
        projectService.deleteProjectById(id);
    }

    @PutMapping("/update/{id}/{newName}")
    @Operation(summary = "Update project name", description = "Updates the name of an existing project")
    public void update(
            @Parameter(description = "ID of the project to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New name for the project", required = true)
            @PathVariable String newName) {
        projectService.updateProjectName(id,newName);
    }

}
