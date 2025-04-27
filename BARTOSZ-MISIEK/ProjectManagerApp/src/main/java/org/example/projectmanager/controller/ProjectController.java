package org.example.projectmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanager.dto.project.ProjectCreateDto;
import org.example.projectmanager.dto.project.ProjectDto;
import org.example.projectmanager.dto.project.ProjectEditDto;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.service.IProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Project", description = "Project management ")
@RequestMapping("/api/projects")
public class ProjectController {
    private final IProjectService service;

    public ProjectController(IProjectService projectService) {
        this.service = projectService;
    }

    @GetMapping("/{id}")
    public ProjectDto get(
            @Parameter(description = "The id of the entity")
            @PathVariable Long id
    ) throws EntityNotFoundException {
        return this.service.getById(id);
    }

    @PostMapping
    public Long create(@RequestBody ProjectCreateDto dto) {
        return this.service.create(dto);
    }

    @PutMapping
    public void update(@RequestBody ProjectEditDto dto) throws EntityNotFoundException {
        this.service.edit(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an entity", responses = {
            @ApiResponse(responseCode = "200", description = "Entity deleted"),
            @ApiResponse(responseCode = "400", description = "Entity not found")
    })
    public void delete(
            @Parameter(description = "The id of the entity")
            @PathVariable Long id
    ) throws EntityNotFoundException {
        this.service.delete(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>("Error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
