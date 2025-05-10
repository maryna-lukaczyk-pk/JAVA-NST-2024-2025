package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.dto.UserDTO;
import org.example.projectmanagerapp.service.UserService;
import org.example.projectmanagerapp.controller.UserController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Testy związane z kontrolerem użytkownika
class UserControllerTest {

    private final UserService userService = mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    @DisplayName("Returns list from service")
    void getAllUsers_success() {
        UserDTO u1 = new UserDTO(1L, "Kacper", List.of());
        UserDTO u2 = new UserDTO(2L, "Jan",   List.of());
        when(userService.getAllUsers()).thenReturn(List.of(u1, u2));

        var result = userController.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Kacper", result.get(0).username());
        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("Returns DTO when found")
    void getUserById_success() {
        UserDTO dto = new UserDTO(5L, "Piotr", List.of(10L));
        when(userService.getUserById(5L)).thenReturn(dto);

        UserDTO result = userController.getUserById(5L);

        assertEquals(5L, result.id());
        assertEquals("Piotr", result.username());
        verify(userService).getUserById(5L);
    }

    @Test
    @DisplayName("404 when not found")
    void getUserById_notFound() {
        when(userService.getUserById(7L)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: 7"));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class, () -> userController.getUserById(7L)
        );
        assertTrue(ex.getMessage().contains("User not found with ID: 7"));
    }

    @Test
    @DisplayName("Forwards body to service")
    void createUser_success() {
        var body = new org.example.projectmanagerapp.entity.User("Dawid");
        var dto  = new UserDTO(9L, "Dawid", List.of());
        when(userService.createUser(body)).thenReturn(dto);

        UserDTO result = userController.createUser(body);

        assertEquals("Dawid", result.username());
        verify(userService).createUser(body);
    }

    @Test
    @DisplayName("Update or 404")
    void updateUser() {
        var body = new org.example.projectmanagerapp.entity.User("Damian");
        var dto  = new UserDTO(4L, "Damian", List.of());
        when(userService.updateUser(4L, body)).thenReturn(dto);

        var ok = userController.updateUser(4L, body);
        assertEquals("Damian", ok.username());

        when(userService.updateUser(5L, body))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: 5"));
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class, () -> userController.updateUser(5L, body)
        );
        assertTrue(ex.getMessage().contains("User not found with ID: 5"));

        verify(userService, times(2)).updateUser(anyLong(), any());
    }

    @Test
    @DisplayName("Void or 404")
    void deleteUser() {
        doNothing().when(userService).deleteUser(11L);
        assertDoesNotThrow(() -> userController.deleteUser(11L));

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: 12")).when(userService).deleteUser(12L);

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class, () -> userController.deleteUser(12L)
        );
        assertTrue(ex.getMessage().contains("User not found with ID: 12"));

        verify(userService, times(2)).deleteUser(anyLong());
    }
}