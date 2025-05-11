package com.example.projectmanagerapp.controller;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean
    UserService userService;

    @Test
    void getAllUsersReturnsJsonArray() throws Exception {
        User u = new User(); u.setId(1L); u.setUsername("ala");
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(u));

        mvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("ala"));
    }

    @Test
    void createUserReturnsCreated() throws Exception {
        User u = new User(); u.setUsername("bob");
        Mockito.when(userService.createUser(any())).thenReturn(u);

        mvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("bob"));
    }

    @Test
    void updateUserReturnsOk() throws Exception {
        User u = new User(); u.setUsername("zed");
        Mockito.when(userService.updateUser(Mockito.eq(5L), any())).thenReturn(u);

        mvc.perform(put("/api/user/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(u)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("zed"));
    }

    @Test
    void deleteUserReturnsNoContent() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(7L);

        mvc.perform(delete("/api/user/7"))
                .andExpect(status().isNoContent());
    }
    @Test
    @DisplayName("getUserById returns JSON user when found")
    void getUserByIdReturnsUser() throws Exception {
        User u = new User();
        u.setId(42L);
        u.setUsername("charlie");
        Mockito.when(userService.getUserById(42L)).thenReturn(u);

        mvc.perform(get("/api/user/42"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.username").value("charlie"));
    }

}