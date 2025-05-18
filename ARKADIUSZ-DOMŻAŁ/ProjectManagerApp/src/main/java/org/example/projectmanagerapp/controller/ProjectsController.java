package org.example.projectmanagerapp.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.projectmanagerapp.entity.Projects;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.service.ProjectService;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/projects")
@Tag(name = "Projects")
public class ProjectsController {
    private final ProjectService projectService;

    public ProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    public static class AssignUserRequest {
        private Long userId;

        public AssignUserRequest() {}

        public AssignUserRequest(Long userId) {
            this.userId = userId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

    @Operation(summary = "Retrieve all Projects", description = "Returns a list of Projects")
    @GetMapping("/get")
    public List<Projects> getAllProjects() {
        return projectService.getAllProjects();
    }

    @Operation(summary = "Create a new Project", description = "Allows to create a new Project")
    @PostMapping("/create")
    public ResponseEntity<Projects> createProject(
            @Parameter(description="Project object that needs to be created", required=true)
            @RequestBody Projects project) {

        Projects createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @Operation(summary = "Update the Project", description = "Updates an existing Project by Id")
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProject(
            @Parameter(description = "The ID of the project to update", required = true)
            @PathVariable Long id,

            @Parameter(description = "The project object containing updated information", required = true)
            @RequestBody Projects updatedProject){

        return projectService.updateProject(id, updatedProject)
                .map(updated -> ResponseEntity.ok("Project updated succesfully")) // zwraca 200 (ok)
                .orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Project with id: " + id +" not found")); //zwróci blad
    }

    @Operation(summary = "Delete the Project", description = "Deletes the project by Id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProject(
            @Parameter(description = "The ID of the project to delete", required = true)
            @PathVariable Long id){

        boolean isDeleted = projectService.deleteProject(id);
        if(isDeleted){
            return new ResponseEntity<>("Project deleted succesfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }

    //metoda wyszukania projektu po jego ID
    @Operation(summary = "Get a Project by ID", description = "Returns a single Project by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        Optional<Projects> projectOpt = projectService.getProjectById(id);
        if (projectOpt.isPresent()) {
            return ResponseEntity.ok(projectOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Project with id: " + id + " not found");
        }
    }

    //dodanie uzytkownika do projektu
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Void> assignUserToProject(
            @PathVariable Long projectId,
            @RequestBody AssignUserRequest assignUserRequest) {

        boolean assigned = projectService.assignUserToProject(projectId, assignUserRequest.getUserId());
        if (assigned) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{projectId}/users")
    public ResponseEntity<List<Users>> getUsersOfProject(@PathVariable Long projectId) {
        List<Users> users = projectService.getUsersOfProject(projectId);
        if (users != null) {
            return ResponseEntity.ok(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
