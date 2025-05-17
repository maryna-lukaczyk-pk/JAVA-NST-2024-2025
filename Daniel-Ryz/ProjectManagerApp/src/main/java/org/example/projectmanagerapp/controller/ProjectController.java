package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.CreateProjectRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Tasks;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor // zastępuje ręczne Dependency Injection
@Tag(name = "Project", description = "Controller for creating, deleting and downloading projects.")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Gets a list of all project.")
    @ApiResponse(responseCode = "200", description = "Project list returned successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Project.class)))
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Get a project by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project returned successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @GetMapping("/{id}")
    public Project getProjectById(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @Operation(summary = "Create a new Project.")
    @ApiResponse(responseCode = "200", description = "Project created successfully.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Project.class)))
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody CreateProjectRequest request) {
        Project project = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @Operation(summary = "Update a project by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project updated successfully.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tasks.class))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @PutMapping("/{id}")
    public Project updateProject(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id, @RequestBody CreateProjectRequest request) {
        return projectService.updateProject(id, request);
    }

    @Operation(summary = "Delete a project by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project deleted successfully.",
                    content = @Content(examples = @ExampleObject())),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content(examples = @ExampleObject()))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @Parameter(description = "Project ID", example = "1")
            @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project deleted successfully.");
    }

}
