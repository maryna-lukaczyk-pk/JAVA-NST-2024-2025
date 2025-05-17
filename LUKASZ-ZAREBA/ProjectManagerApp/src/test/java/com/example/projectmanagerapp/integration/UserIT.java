package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)

public class UserIT extends BaseIT {

    @Test
    public void testUserConstructor() {
        // Test konstruktora z parametrami
        User user = new User(1, "testuser");
        assertEquals(1, user.getId());
        assertEquals("testuser", user.getUsername());
    }

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateNewUser() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("user1");
        String userJson1 = objectMapper.writeValueAsString(user1);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson1))
                .andExpect(status().isOk());

        User user2 = new User();
        user2.setUsername("user2");
        String userJson2 = objectMapper.writeValueAsString(user2);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson2))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setUsername("singleUser");
        String userJson = objectMapper.writeValueAsString(user);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(response, User.class);

        mockMvc.perform(get("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("singleUser")));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setUsername("oldName");
        String userJson = objectMapper.writeValueAsString(user);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(response, User.class);

        User updated = new User();
        updated.setUsername("newName");
        String updateJson = objectMapper.writeValueAsString(updated);
        mockMvc.perform(put("/api/users/{id}", createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newName")));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("toDelete");
        String userJson = objectMapper.writeValueAsString(user);
        String response = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User createdUser = objectMapper.readValue(response, User.class);

        mockMvc.perform(delete("/api/users/{id}", createdUser.getId()))
                .andExpect(status().isNoContent());
    }
}
