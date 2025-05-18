package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should assign user to project")
    void assignUserToProject() throws Exception {
        // a) Create user
        User user = new User();
        user.setUsername("integration_user");

        String userJson = objectMapper.writeValueAsString(user);

        String userResponse = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User savedUser = objectMapper.readValue(userResponse, User.class);

        // b) Create project
        Project project = new Project("Integration Project", "Testing integration", "ACTIVE");

        String projectJson = objectMapper.writeValueAsString(project);

        String projectResponse = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Project savedProject = objectMapper.readValue(projectResponse, Project.class);

        // c) Assign user to project
        mockMvc.perform(post("/api/projects/" + savedProject.getId() + "/users")
                        .param("userId", String.valueOf(savedUser.getId())))
                .andExpect(status().isOk());

        // d) Verify user is in project member list
        mockMvc.perform(get("/api/projects/" + savedProject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].id").value(savedUser.getId()));
    }
}
