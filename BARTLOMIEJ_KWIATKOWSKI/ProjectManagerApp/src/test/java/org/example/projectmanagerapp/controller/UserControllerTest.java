package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController();
        // wstrzykujemy mockowany service do controllera (przez refleksję lub setter)
        // tutaj wykorzystam ustawienie pola bez setterów i @Autowired
        // bez setterów potrzebna refleksja:
        try {
            java.lang.reflect.Field userServiceField = UserController.class.getDeclaredField("userService");
            userServiceField.setAccessible(true);
            userServiceField.set(userController, userService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllUsers() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("user1");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("user2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<Users> users = userController.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserByIdFound() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("foundUser");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<Users> response = userController.getUserById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("foundUser", response.getBody().getUsername());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Users> response = userController.getUserById(1L);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testCreateUser() {
        Users inputUser = new Users();
        inputUser.setUsername("newUser");

        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setUsername("newUser");

        when(userService.createUser(any(Users.class))).thenReturn(savedUser);

        Users result = userController.createUser(inputUser);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newUser", result.getUsername());

        verify(userService, times(1)).createUser(inputUser);
    }

    @Test
    void testUpdateUserFound() {
        Long id = 1L;
        Users inputUser = new Users();
        inputUser.setUsername("updatedUser");

        Users updatedUser = new Users();
        updatedUser.setId(id);
        updatedUser.setUsername("updatedUser");

        when(userService.updateUser(eq(id), any(Users.class))).thenReturn(updatedUser);

        ResponseEntity<Users> response = userController.updateUser(id, inputUser);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("updatedUser", response.getBody().getUsername());

        verify(userService, times(1)).updateUser(id, inputUser);
    }

    @Test
    void testUpdateUserNotFound() {
        Long id = 1L;
        Users inputUser = new Users();
        inputUser.setUsername("updatedUser");

        when(userService.updateUser(eq(id), any(Users.class))).thenReturn(null);

        ResponseEntity<Users> response = userController.updateUser(id, inputUser);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(userService, times(1)).updateUser(id, inputUser);
    }

    @Test
    void testDeleteUserFound() {
        Long id = 1L;

        when(userService.getUserById(id)).thenReturn(Optional.of(new Users()));

        ResponseEntity<Void> response = userController.deleteUser(id);

        assertEquals(204, response.getStatusCodeValue());

        verify(userService, times(1)).getUserById(id);
        verify(userService, times(1)).deleteUser(id);
    }

    @Test
    void testDeleteUserNotFound() {
        Long id = 1L;

        when(userService.getUserById(id)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = userController.deleteUser(id);

        assertEquals(404, response.getStatusCodeValue());

        verify(userService, times(1)).getUserById(id);
        verify(userService, never()).deleteUser(anyLong());
    }
}