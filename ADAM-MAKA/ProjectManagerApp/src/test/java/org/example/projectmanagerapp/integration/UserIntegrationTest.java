package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser() throws Exception {
        String postResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int userId = objectMapper.readTree(postResponse).get("id").asInt();

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId)))
                .andExpect(jsonPath("$.username", is("john")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    void updateUser() throws Exception {
        String postResponse = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"john\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int userId = objectMapper.readTree(postResponse).get("id").asInt();

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"jane\",\"email\":\"jane@example.com\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId)))
                .andExpect(jsonPath("$.username", is("jane")))
                .andExpect(jsonPath("$.email", is("jane@example.com")));
    }

    @Test
    void deleteUser() throws Exception {
        // Create a user
        JsonNode created = objectMapper.readTree(
                mockMvc.perform(post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"john\",\"email\":\"john@example.com\"}"))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString()
        );
        int userId = created.get("id").asInt();

        // Delete the user
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk());

        // Ensure it no longer exists
        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isNotFound());
    }
}
