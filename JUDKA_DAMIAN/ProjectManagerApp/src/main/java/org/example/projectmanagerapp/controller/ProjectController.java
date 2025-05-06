package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.schemes.ProjectDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectController {
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @PostMapping
    @Operation(summary = "Create a new project",
            description = "Create a new project in database")
    public ResponseEntity<Map<String, String>> createProject(@RequestBody ProjectDTO projectDTO) {
        Map<String, String> response = new HashMap<>();

        Project project = new Project();
        project.setName(projectDTO.getName());

        Project savedProject = projectRepository.save(project);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProject.getId()).toUri();

        response.put("success", "Project created");
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all projects",
            description = "Get a list of all projects from the database")
    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project",
            description = "Delete a project by ID from database")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Project ID")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        if(projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            response.put("success", "Project deleted");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project",
            description = "Update project attributes by ID")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Project ID")
    public ResponseEntity<Map<String, String>> updateProject(@RequestBody ProjectDTO projectDTO, @PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        if(projectRepository.existsById(id)) {
            Project project = new Project();
            project.setId(id);
            project.setName(projectDTO.getName());
            projectRepository.save(project);
            response.put("success", "Project updated");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
