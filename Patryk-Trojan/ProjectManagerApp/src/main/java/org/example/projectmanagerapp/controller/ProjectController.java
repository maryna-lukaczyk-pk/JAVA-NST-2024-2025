package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name="Project",description = "Operations for managing project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {

        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Return all Project",description = "Return a list of all projects from DB")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Return a Project",description = "Return a Project by id from DB")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }


    @PostMapping
    @Operation(summary = "Create a new Project",description = "Adds new Project to DB from DB")
    public Project createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Project",description = "Deleting a Project by id from DB")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a Project",description = "Update a Project by id from DB")
    public void updateProject(@RequestBody Project project,@PathVariable Long id) {
        projectService.updateProject(id, project);
    }
}


