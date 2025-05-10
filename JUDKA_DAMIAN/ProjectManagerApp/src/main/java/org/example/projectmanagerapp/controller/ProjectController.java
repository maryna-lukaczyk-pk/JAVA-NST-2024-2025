package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.schemas.ProjectDTO;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operations for managing projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @Operation(summary = "Create a new project",
            description = "Create a new project in database")
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO) {
        projectService.addProject(projectDTO);
        return new ResponseEntity<>("Project created", HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all projects",
            description = "Get a list of all projects from the database")
    public List<Project> getProjects() {
        return projectService.findAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a single project",
            description = "Get a single project with given ID",
            responses = {@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Project.class)))})
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Project ID")
    public ResponseEntity<?> getProjectById(@PathVariable Integer id) {
        try {
            Project project = projectService.findProjectById(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (NotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Project not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project",
            description = "Delete a project by ID from database")
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Project ID")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            projectService.deleteProject(id);
            response.put("success", "Project deleted");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
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
        try {
            projectService.updateProject(projectDTO, id);
            response.put("success", "Project updated");
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            response.put("error", "Project not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
