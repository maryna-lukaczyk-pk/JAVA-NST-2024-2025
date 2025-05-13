package com.example.projectmanagerapp.IntegrationTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.ProjectManagerAppApplication;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAssignUserToProject() throws Exception {
        User user = new User();
        user.setUsername("TestUser2");
        String jsonUser = objectMapper.writeValueAsString(user);

        String createdUserResponse = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        User createdUser = objectMapper.readValue(createdUserResponse, User.class);
        Long userId = createdUser.getId();

        Project project = new Project();
        project.setName("IntegrationProject");
        String jsonProject = objectMapper.writeValueAsString(project);

        String createdProjectResponse = mockMvc.perform(post("/api/projects/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonProject))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Project createdProject = objectMapper.readValue(createdProjectResponse, Project.class);
        Long projectId = createdProject.getId();

        mockMvc.perform(put("/api/projects/" + projectId + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userId)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name").value("IntegrationProject"))
                .andExpect(jsonPath("$.users[0].username").value("TestUser2"));
    }
}
