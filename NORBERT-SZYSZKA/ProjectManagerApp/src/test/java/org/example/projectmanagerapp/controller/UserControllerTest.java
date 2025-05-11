package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock private UserService userService;
    @InjectMocks private UserController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllUsers() throws Exception {
        User u1 = new User(); u1.setId(1L); u1.setUsername("alice");
        User u2 = new User(); u2.setId(2L); u2.setUsername("bob");
        when(userService.findAll()).thenReturn(Arrays.asList(u1, u2));

        mvc.perform(get("/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].username").value("bob"));
    }

    @Test
    void getUserById_found() throws Exception {
        User u = new User(); u.setId(1L); u.setUsername("alice");
        when(userService.findById(1L)).thenReturn(Optional.of(u));

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"));
    }

    @Test
    void getUserById_notFound() throws Exception {
        when(userService.findById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser() throws Exception {
        User u = new User(); u.setId(1L); u.setUsername("alice");
        when(userService.create(any(User.class))).thenReturn(u);

        mvc.perform(post("/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"alice\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateUser_found() throws Exception {
        User existing = new User(); existing.setId(1L);
        when(userService.findById(1L)).thenReturn(Optional.of(existing));
        User updated = new User(); updated.setId(1L); updated.setUsername("bob");
        when(userService.update(eq(1L), any(User.class))).thenReturn(updated);

        mvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"bob\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
        verify(userService).delete(1L);
    }
}
