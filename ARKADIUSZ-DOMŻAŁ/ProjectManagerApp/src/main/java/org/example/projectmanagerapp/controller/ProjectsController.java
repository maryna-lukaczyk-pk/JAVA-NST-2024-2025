package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
@RestController
@RequestMapping("api/projects")
@Tag(name = "Projects")
public class ProjectsController {
    private final ProjectRepository projectRepository;

    public ProjectsController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Operation(summary = "Retrieve all Projects", description = "Returns a list of Projects")
    @GetMapping("/get")
    public List<Projects> getAllProjects() {
        return projectRepository.findAll();
    }

    @Operation(summary = "Create a new Project", description = "Allows to create a new Project")
    @PostMapping("/create")
    public ResponseEntity<Projects> createProject(@RequestBody @Parameter(description="Project object that needs to be created") Projects project) {
        Projects savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);
    }
}
