package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.ProjectManagerAppApplication;
import org.example.projectmanagerapp.schemas.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ProjectManagerAppApplication.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        UserDTO dto = new UserDTO();
        dto.setUsername("user123");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // Assuming the user has been added
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Create user
        UserDTO dto = new UserDTO();
        dto.setUsername("Zosia31");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Fetching the list of users and get the last ID
        MvcResult getAll = mockMvc.perform(get("/api/users"))
                .andReturn();

        List<Map<String, Object>> users = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) (users.get(users.size() - 1).get("id"));

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("Zosia31")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // Add a new user
        UserDTO dto = new UserDTO();
        dto.setUsername("originalUsername");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Get ID
        MvcResult getAll = mockMvc.perform(get("/api/users"))
                .andReturn();

        List<Map<String, Object>> users = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) (users.get(users.size() - 1).get("id"));

        // Update a user
        dto.setUsername("updatedUsername");

        mockMvc.perform(put("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // Check update result
        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updatedUsername")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // Add a user
        UserDTO dto = new UserDTO();
        dto.setUsername("usernameToDelete");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        // Get ID
        MvcResult getAll = mockMvc.perform(get("/api/users"))
                .andReturn();

        List<Map<String, Object>> users = objectMapper.readValue(
                getAll.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );
        Integer id = (Integer) (users.get(users.size() - 1).get("id"));

        // Delete
        mockMvc.perform(delete("/api/users/" + id))
                .andExpect(status().isOk());

        // Check if user is deleted
        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isNotFound());
    }
}
