package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ProjectIntegrationTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    Long userId, projectId;

    @BeforeEach
    void setup() throws Exception {
        // Tworzymy użytkownika przez API
        Users user = new Users();
        user.setUsername("testuser");
        String userJson = mapper.writeValueAsString(user);

        String userResp = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        userId = mapper.readValue(userResp, Users.class).getId();

        // Tworzymy projekt przez API
        Project project = new Project();
        project.setName("proj");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);

        String projResp = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        projectId = mapper.readValue(projResp, Project.class).getId();
    }

    @Test
    void shouldAddUserToProject() throws Exception {
        mvc.perform(put("/api/projects/" + projectId + "/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue())));
    }
}