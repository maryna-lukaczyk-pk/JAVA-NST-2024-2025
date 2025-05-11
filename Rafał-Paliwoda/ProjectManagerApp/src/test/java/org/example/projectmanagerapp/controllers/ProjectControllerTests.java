package org.example.projectmanagerapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Task;
import org.example.projectmanagerapp.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProjectControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private ObjectMapper objectMapper;
    private Project testProject;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
        objectMapper = new ObjectMapper();

        testProject = new Project();
        testProject.setId(1L);
        testProject.setName("Test Project");
    }

    @Test
    @DisplayName("It should get project by ID")
    void getProject() throws Exception {
        when(projectService.findProjectById(1L)).thenReturn(Optional.of(testProject));

        mockMvc.perform(get("/api/v1/project/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Project"));

        verify(projectService, times(1)).findProjectById(1L);
    }

    @Test
    @DisplayName("It should return 404 when project not found")
    void getProjectNotFound() throws Exception {
        when(projectService.findProjectById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/project/999"))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).findProjectById(999L);
    }

    @Test
    @DisplayName("It should get all projects")
    void getAllProjects() throws Exception {
        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project 2");

        List<Project> projects = Arrays.asList(testProject, project2);
        when(projectService.findAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/api/v1/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Project"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Project 2"));

        verify(projectService, times(1)).findAllProjects();
    }

    @Test
    @DisplayName("It should create a new project")
    void createProject() throws Exception {
        Project newProject = new Project();
        newProject.setName("New Project");

        Project savedProject = new Project();
        savedProject.setId(3L);
        savedProject.setName("New Project");

        when(projectService.createProject(any(Project.class))).thenReturn(savedProject);

        mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("New Project"));

        verify(projectService, times(1)).createProject(any(Project.class));
    }

    @Test
    @DisplayName("It should update an existing project")
    void updateProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setId(1L);
        updatedProject.setName("Updated Project");

        when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(updatedProject);

        mockMvc.perform(put("/api/v1/project/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Project"));

        verify(projectService, times(1)).updateProject(eq(1L), any(Project.class));
    }

    @Test
    @DisplayName("It should delete a project")
    void deleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(1L);

        mockMvc.perform(delete("/api/v1/project/1"))
                .andExpect(status().isNoContent());

        verify(projectService, times(1)).deleteProject(1L);
    }

    @Test
    @DisplayName("It should get project tasks")
    void getProjectTasks() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        when(projectService.getProjectTasks(1L)).thenReturn(tasks);

        mockMvc.perform(get("/api/v1/project/1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(projectService, times(1)).getProjectTasks(1L);
    }

    @Test
    @DisplayName("It should assign user to project")
    void assignUserToProject() throws Exception {
        Long userId = 2L;
        doNothing().when(projectService).assignUserToProject(1L, userId);

        mockMvc.perform(post("/api/v1/project/1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId)))
                .andExpect(status().isCreated());

        verify(projectService, times(1)).assignUserToProject(1L, userId);
    }

    @Test
    @DisplayName("It should return 500 when error occurs during user assignment")
    void assignUserToProjectError() throws Exception {
        Long userId = 2L;
        doThrow(new RuntimeException("Something went wrong"))
                .when(projectService).assignUserToProject(1L, userId);

        mockMvc.perform(post("/api/v1/project/1/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId)))
                .andExpect(status().isInternalServerError());

        verify(projectService, times(1)).assignUserToProject(1L, userId);
    }

    @Test
    @DisplayName("It should handle ResponseStatusException when updating project")
    void updateProjectNotFound() throws Exception {
        Project updateRequest = new Project();
        updateRequest.setName("Updated Name");

        when(projectService.updateProject(eq(999L), any(Project.class)))
                .thenThrow(new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Projekt o ID 999 nie został znaleziony"));

        mockMvc.perform(put("/api/v1/project/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).updateProject(eq(999L), any(Project.class));
    }

    @Test
    @DisplayName("It should handle ResponseStatusException when deleting project")
    void deleteProjectNotFound() throws Exception {
        doThrow(new ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND,
                "Projekt o ID 999 nie został znaleziony"))
                .when(projectService).deleteProject(999L);

        mockMvc.perform(delete("/api/v1/project/999"))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).deleteProject(999L);
    }

    @Test
    @DisplayName("It should handle ResponseStatusException when getting project tasks")
    void getProjectTasksProjectNotFound() throws Exception {
        when(projectService.getProjectTasks(999L))
                .thenThrow(new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Projekt o ID 999 nie został znaleziony"));

        mockMvc.perform(get("/api/v1/project/999/tasks"))
                .andExpect(status().isNotFound());

        verify(projectService, times(1)).getProjectTasks(999L);
    }
}