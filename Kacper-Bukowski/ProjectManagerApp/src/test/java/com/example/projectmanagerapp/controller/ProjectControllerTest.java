package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.ProjectRepository;
import com.example.projectmanagerapp.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Project project1;
    private Project project2;

    @BeforeEach
    void setUp() {
        project1 = new Project();
        project1.setName("TestProject1");

        project2 = new Project();
        project2.setName("TestProject2");

        User user = new User();
        user.setUsername("TestUser");
        Set<User> users = new HashSet<>();
        users.add(user);
        project2.setUsers(users);
    }

    @Test
    @DisplayName("Should return all projects")
    void getAllProjects() throws Exception {
        List<Project> projects = Arrays.asList(project1, project2);

        when(projectService.getAllProjects()).thenReturn(projects);

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("TestProject1")))
                .andExpect(jsonPath("$[1].name", is("TestProject2")));

        verify(projectService, times(1)).getAllProjects();
    }

    @Test
    @DisplayName("Should return project by ID")
    void getProjectById() throws Exception {
        when(projectService.getProjectById(1L)).thenReturn(project1);

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestProject1")));

        verify(projectService, times(1)).getProjectById(1L);
    }

    @Test
    @DisplayName("Should create a new project")
    void createProject() throws Exception {
        when(projectService.createProject(any(Project.class))).thenReturn(project1);

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestProject1")));

        verify(projectService, times(1)).createProject(any(Project.class));
    }

    @Test
    @DisplayName("Should update project")
    void updateProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setName("UpdatedProject");

        User user = new User();
        user.setUsername("UpdatedUser");
        Set<User> users = new HashSet<>();
        users.add(user);
        updatedProject.setUsers(users);

        when(projectService.updateProject(eq(1L), any(Project.class))).thenReturn(updatedProject);

        mockMvc.perform(put("/projects/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("UpdatedProject")))
                .andExpect(jsonPath("$.users[0].username", is("UpdatedUser")));

        verify(projectService, times(1)).updateProject(eq(1L), any(Project.class));
    }

    @Test
    @DisplayName("Should delete project")
    void deleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(1L);

        mockMvc.perform(delete("/projects/1"))
                .andExpect(status().isNoContent());

        verify(projectService, times(1)).deleteProject(1L);
    }

    @Test
    @DisplayName("Should assign a user to a project")
    void assignUserToProject() throws Exception {
        Project testProject = new Project();
        setIdField(testProject, 1L);
        testProject.setName("TestProject");

        User testUser = new User();
        setIdField(testUser, 1L);
        testUser.setUsername("TestUser");

        Project resultProject = new Project();
        setIdField(resultProject, 1L);
        resultProject.setName("TestProject");
        Set<User> users = new HashSet<>();
        users.add(testUser);
        resultProject.setUsers(users);

        when(projectService.addUserToProject(1L, 1L)).thenReturn(resultProject);

        mockMvc.perform(post("/projects/1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("TestProject")))
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].username", is("TestUser")));

        verify(projectService, times(1)).addUserToProject(1L, 1L);
    }

    private void setIdField(Object entity, Long id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set ID field", e);
        }
    }
}
