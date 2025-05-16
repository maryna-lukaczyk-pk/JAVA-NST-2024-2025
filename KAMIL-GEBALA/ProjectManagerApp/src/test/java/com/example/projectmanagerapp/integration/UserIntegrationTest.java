package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.config.TestConfig;
import com.example.projectmanagerapp.entity.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestConfig.class)
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateUser() throws Exception {
        Users user = new Users();
        user.setUsername("testCreateUser");

        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is("testCreateUser")));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        Users user = new Users();
        user.setUsername("testGetAllUser");

        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())))
                .andExpect(jsonPath("$[*].username", hasItem("testGetAllUser")));
    }

    @Test
    public void testUpdateUser() throws Exception {
        Users user = new Users();
        user.setUsername("testUpdateUserBefore");

        String userJson = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

        Users createdUser = objectMapper.readValue(
                result.getResponse().getContentAsString(), Users.class);

        createdUser.setUsername("testUpdateUserAfter");
        String updatedUserJson = objectMapper.writeValueAsString(createdUser);
        mockMvc.perform(put("/api/users/update/" + createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testUpdateUserAfter")));
    }

    @Test
    public void testDeleteUser() throws Exception {
        Users user = new Users();
        user.setUsername("testDeleteUser");

        String userJson = objectMapper.writeValueAsString(user);
        MvcResult result = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

        Users createdUser = objectMapper.readValue(
                result.getResponse().getContentAsString(), Users.class);

        mockMvc.perform(delete("/api/users/delete/" + createdUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].username", not(hasItem("testDeleteUser"))));
    }
}
