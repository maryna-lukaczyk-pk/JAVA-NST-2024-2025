package com.example.projectmanagerapp.controller;
import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.service.ProjectService;
import com.example.projectmanagerapp.service.UsersService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;


@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project Controller")
public class ProjectController {
    @Autowired
    private final ProjectService projectService;
    @Autowired
    private UsersService usersService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/all")
    @Operation  (summary = "Get all projects",description = "Returns list of all projects")
    public List<Project> getProjects() { return projectService.getAllProjects(); }

    @PostMapping("/create")
    @Operation  (summary = "Create new project",description = "Adds new project to database")
    public ResponseEntity<Project> createProject(@Parameter (description = "Project object",required = true) @RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation (summary = "Update project", description = "Updates informations about project")
    public ResponseEntity<Project> updateProject(
            @Parameter (description="ID of the project",required = true) @PathVariable long id,
            @Parameter (description = "Project object",required = true)@RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation (summary = "Deletes project", description = "Deletes project from database by id")
    public void deleteProject(@Parameter (description="ID of the project",required = true) @PathVariable long id) {
        projectService.deleteProject(id);
    }

    @PostMapping("/{projectId}/users/{userId}")
    @Operation (summary = "Assign user to project", description = "Assigns a user to a project")
    public ResponseEntity<?> assignUserToProject(
            @Parameter (description="ID of the project",required = true) @PathVariable Long projectId,
            @Parameter (description="ID of the user",required = true) @PathVariable Long userId) {
        ProjectUser projectUser = projectService.assignUserToProject(projectId, userId);
        if (projectUser != null) {
            return new ResponseEntity<>(projectUser, HttpStatus.OK);
        }
        return new ResponseEntity<>("Project or user not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{projectId}/users")
    @Operation (summary = "Get users in project", description = "Returns all users assigned to a project")
    public ResponseEntity<?> getUsersInProject(
            @Parameter (description="ID of the project",required = true) @PathVariable Long projectId) {
        List<Users> users = projectService.getUsersInProject(projectId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
