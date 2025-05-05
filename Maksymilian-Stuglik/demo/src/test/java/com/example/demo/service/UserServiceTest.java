package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return all users")
    void getAllUsers() {
        Users user1 = new Users();
        user1.setId(1L);
        user1.setUsername("TestUser1");

        Users user2 = new Users();
        user2.setId(2L);
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<Users> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("TestUser1", result.get(0).getUsername());
        assertEquals("TestUser2", result.get(1).getUsername());

    }

    @Test
    @DisplayName("Should create a new user")
    void createUser() {
        Users user = new Users();
        user.setUsername("NewUser");

        when(userRepository.save(any(Users.class))).thenReturn(user);

        Users result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("NewUser", result.getUsername());

    }

    @Test
    @DisplayName("Should update an existing user")
    void updateUser() {
        Long userId = 1L;
        Users updatedUser = new Users();
        updatedUser.setUsername("UpdatedUser");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.save(any(Users.class))).thenReturn(updatedUser);

        Users result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("UpdatedUser", result.getUsername());
        assertEquals(userId, updatedUser.getId());


    }

    @Test
    @DisplayName("Should delete a user")
    void deleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

    }
}