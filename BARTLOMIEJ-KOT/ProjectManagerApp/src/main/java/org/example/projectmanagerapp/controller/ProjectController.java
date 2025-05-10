package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Projects", description = "Operacje na projektach")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Pobierz wszystkie projekty")
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }


    @Operation(summary = "Dodaj nowy projekt")
    @PostMapping
    public Project createProject(
            @Parameter(description = "Dane nowego projektu") @RequestBody Project project
    ) {
        return projectService.createProject(project);
    }

    @Operation(summary = "Zaktualizuj projekt")
    @PutMapping("/{id}")
    public Project updateProject(
            @Parameter(description = "project ID do zmiany") @PathVariable Long id,
            @Parameter(description = "nowe dane") @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @Operation(summary = "Usuń projekt")
    @DeleteMapping("/{id}")
    public void deleteProject(@Parameter(description = "project ID do usuniecia") @PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
