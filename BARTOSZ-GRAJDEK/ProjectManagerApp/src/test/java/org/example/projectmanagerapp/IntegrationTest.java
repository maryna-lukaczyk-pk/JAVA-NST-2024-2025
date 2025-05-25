package org.example.projectmanagerapp;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddProjectToUser() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setProjects(Set.of());

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserResponse = mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.projects").isArray())
                .andExpect(jsonPath("$.projects").isEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(createdUserResponse, User.class);
        Long userId = createdUser.getId();

        Project project = new Project();
        project.setName("testProject");
        String projectJson = objectMapper.writeValueAsString(project);

        String createdProjectResponse = mvc.perform(post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("testProject"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Project createdProject = objectMapper.readValue(createdProjectResponse, Project.class);

        createdUser.setProjects(Set.of(createdProject));
        userJson = objectMapper.writeValueAsString(createdUser);

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(put("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk());

        mvc.perform(get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.projects").isArray())
                .andExpect(jsonPath("$.projects[0].name").value("testProject"));

        mvc.perform(delete("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mvc.perform(get("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        Long projectId = createdProject.getId();
        mvc.perform(delete("/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mvc.perform(get("/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(delete("/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}