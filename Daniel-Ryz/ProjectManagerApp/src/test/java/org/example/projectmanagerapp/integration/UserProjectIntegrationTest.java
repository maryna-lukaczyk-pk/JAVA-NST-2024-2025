package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.dto.CreateUserRequest;
import org.example.projectmanagerapp.entity.Project;
import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserProjectIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private ProjectRepository projectRepository;

    private Users createUser(String username) throws Exception {
        CreateUserRequest request = new CreateUserRequest(username);

        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, Users.class);
    }

    @Test
    void shouldCreateAndGetUser() throws Exception {
        Users user = createUser("testUser");

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        Users user = createUser("toUpdate");

        CreateUserRequest update = new CreateUserRequest("updatedUser");

        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedUser"));
    }

    @Test
    void shouldAssignProjectToUser() throws Exception {
        Users user = createUser("assigner");

        Project project = new Project();
        project.setName("Important Project");
        project = projectRepository.save(project);

        mockMvc.perform(post("/api/users/" + user.getId() + "/projects/" + project.getId()))
                .andExpect(status().isOk());

        Users updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.getProjects()).anyMatch(p -> p.getName().equals("Important Project"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        Users user = createUser("toDelete");

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully."));

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void shouldListAllUsers() throws Exception {
        createUser("user1");
        createUser("user2");

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

}
