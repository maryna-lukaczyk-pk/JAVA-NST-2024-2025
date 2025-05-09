package com.example;


import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.DisplayName;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }
    @Test
    @DisplayName("Should return all users")
    public void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestUser", result.get().getUsername());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    public void testCreateUser() {
        User user = new User();
        user.setUsername("NewUser");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertEquals("NewUser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update existing user")
    public void testUpdateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("OldUsername");

        User updatedUser = new User();
        updatedUser.setUsername("UpdatedUsername");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> result = userService.updateUser(1L, updatedUser);

        assertTrue(result.isPresent());
        assertEquals("UpdatedUsername", result.get().getUsername());

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should delete user by ID")
    public void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
    @Test
    @DisplayName("Should throw exception when user not found")
    public void testGetUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            userService.getUserById(999L);
        });

        verify(userRepository, times(1)).findById(999L);
    }

}
