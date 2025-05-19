package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.projectmanagerapp.service.ProjectService;


import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operacje CRUD na projektach")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(
            summary = "Pobierz wszystkie projekty",
            description = "Zwraca listę wszystkich projektów zapisanych w bazie danych"
    )
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz projekt po ID", description = "Zwraca projekt na podstawie ID")
    public Project getProjectById(
            @Parameter(description = "ID projektu", required = true)
            @PathVariable Long id) {
        return projectService.getProjectById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    @PostMapping
    @Operation(
            summary = "Utwórz nowy projekt",
            description = "Zapisuje projekt i zwraca utworzony obiekt"
    )
    public Project createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dane nowego projektu w formacie JSON",
                    required = true
            )
            @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Aktualizuj projekt",
            description = "Aktualizuje dane projektu na podstawie ID"
    )
    public Project updateProject(
            @Parameter(description = "ID projektu, który ma zostać zaktualizowany", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nowe dane projektu do aktualizacji",
                    required = true
            )
            @RequestBody Project updatedProject) {
        return projectService.updateProject(id, updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Usuń projekt",
            description = "Usuwa projekt na podstawie ID"
    )
    public void deleteProject(
            @Parameter(description = "ID projektu, który ma zostać usunięty", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
    }

}


