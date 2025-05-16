package com.example.projectmanagerapp.integration;

import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.repository.users_repository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class users_controller_integration_test extends integration_test_base {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private users_repository usersRepository;

    @AfterEach
    void cleanup() {
        usersRepository.deleteAll();
    }

    @Test
    @DisplayName("Create User - Integration Test")
    void test_create_user() throws Exception {
        users user = new users();
        user.setUsername("testuser");

        ResultActions response = mockMvc.perform(post("/api/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    @DisplayName("Get All Users - Integration Test")
    void test_get_all_users() throws Exception {
        users user1 = new users();
        user1.setUsername("user1");
        usersRepository.save(user1);

        users user2 = new users();
        user2.setUsername("user2");
        usersRepository.save(user2);

        ResultActions response = mockMvc.perform(get("/api/users"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is("user1")))
                .andExpect(jsonPath("$[1].username", is("user2")));
    }

    @Test
    @DisplayName("Get User By ID - Integration Test")
    void test_get_user_by_id() throws Exception {
        users user = new users();
        user.setUsername("getTestUser");
        users savedUser = usersRepository.save(user);

        ResultActions response = mockMvc.perform(get("/api/users/{id}", savedUser.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())));
    }

    @Test
    @DisplayName("Update User - Integration Test")
    void test_update_user() throws Exception {
        users user = new users();
        user.setUsername("oldUsername");
        users savedUser = usersRepository.save(user);

        users updatedUser = new users();
        updatedUser.setUsername("newUsername");

        ResultActions response = mockMvc.perform(put("/api/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("newUsername")));
    }

    @Test
    @DisplayName("Delete User - Integration Test")
    void test_delete_user() throws Exception {
        users user = new users();
        user.setUsername("deleteTestUser");
        users savedUser = usersRepository.save(user);

        ResultActions response = mockMvc.perform(delete("/api/users/{id}", savedUser.getId()));

        response.andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", savedUser.getId()))
                .andExpect(status().isNotFound());
    }
}