package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.service.ProjectService;


import java.util.List;
@RestController
@RequestMapping("api/projects")
@Tag(name = "Projects")
public class ProjectsController {
    private final ProjectService projectService;

    public ProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Retrieve all Projects", description = "Returns a list of Projects")
    @GetMapping("/get")
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create a new Project", description = "Allows to create a new Project")
    @PostMapping("/create")
    public ResponseEntity<Projects> createProject(@RequestBody @Parameter(description="Project object that needs to be created") Projects project) {
        Projects createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
}
