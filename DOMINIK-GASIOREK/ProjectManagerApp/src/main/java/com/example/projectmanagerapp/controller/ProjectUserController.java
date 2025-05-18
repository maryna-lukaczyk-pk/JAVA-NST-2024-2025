package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.ProjectUser;
import com.example.projectmanagerapp.service.ProjectUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name="ProjectUser Controller", description="User to Projects assigment")
public class ProjectUserController {

    private final ProjectUserService service;

    @Operation(summary="Assign user to project", description="Assignes existing user to an existing project")
    @PostMapping("/add-user-to-project")
    public ResponseEntity<ProjectUser> addUserToProject(
            @RequestParam Long userId,
            @RequestParam Long projectId) {
        ProjectUser created = service.addUserToProject(userId, projectId);
        return ResponseEntity.ok(created);
    }
}
