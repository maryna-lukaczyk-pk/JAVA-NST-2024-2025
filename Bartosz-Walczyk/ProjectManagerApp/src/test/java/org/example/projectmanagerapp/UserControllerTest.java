package org.example.projectmanagerapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.controller.UserController;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("GET /api/users should return list of users")
    void testGetUsers() throws Exception {
        User u1 = new User(); u1.setId(1L); u1.setUsername("User1");
        User u2 = new User(); u2.setId(2L); u2.setUsername("User2");
        List<User> users = Arrays.asList(u1, u2);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("GET /api/users/{id} should return a user when found")
    void testGetUserByIdFound() throws Exception {
        User u = new User(); u.setId(5L); u.setUsername("FoundUser");
        when(userService.getUserById(5L)).thenReturn(u);

        mockMvc.perform(get("/api/users/5"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(u)));

        verify(userService).getUserById(5L);
    }

    @Test
    @DisplayName("POST /api/users should create and return new user")
    void testCreateUser() throws Exception {
        User input = new User(); input.setUsername("NewUser");
        User saved = new User(); saved.setId(10L); saved.setUsername("NewUser");
        when(userService.createUser(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(saved)));

        verify(userService).createUser(any(User.class));
    }

    @Test
    @DisplayName("PUT /api/users should update and return the user")
    void testUpdateUser() throws Exception {
        User update = new User(); update.setId(15L); update.setUsername("UpdatedUser");
        when(userService.updateUser(any(User.class))).thenReturn(update);

        mockMvc.perform(put("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(update)));

        verify(userService).updateUser(any(User.class));
    }

    @Test
    @DisplayName("DELETE /api/users should delete and return the user")
    void testDeleteUser() throws Exception {
        User deleted = new User(); deleted.setId(20L); deleted.setUsername("ToDelete");
        when(userService.deleteUser(eq(20L))).thenReturn(deleted);

        mockMvc.perform(delete("/api/users")
                        .param("id", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(deleted)));

        verify(userService).deleteUser(20L);
    }

    @Test
    @DisplayName("DELETE /api/users should handle null return")
    void testDeleteUserNotFound() throws Exception {
        when(userService.deleteUser(eq(99L))).thenReturn(null);

        mockMvc.perform(delete("/api/users")
                        .param("id", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(userService).deleteUser(99L);
    }
}