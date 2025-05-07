package org.example.projectmanager.service;

import org.example.projectmanager.entity.Users;
import org.example.projectmanager.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UsersRepository userRepository;
    private UsersService userService;

    @BeforeEach
    public void setUp() {
        userRepository = mock(UsersRepository.class);
        userService = new UsersService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    public void testGetAllUsers() {
        Users user1 = new Users();
        user1.setUsername("TestUser1");

        Users user2 = new Users();
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<Users> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    public void testGetByID() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<Users> result = userService.getUsersById(1L);

        assertTrue(result.isPresent());
        assertEquals("TestUser", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    public void testCreateUser() {
        Users newUser = new Users();
        newUser.setUsername("NewUser");

        when(userRepository.save(newUser)).thenReturn(newUser);

        Users createdUser = userService.createUsers(newUser);

        assertNotNull(createdUser);
        assertEquals("NewUser", createdUser.getUsername());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Should update an existing user")
    public void testUpdateUser() {
        Users existingUser = new Users();
        existingUser.setId(1L);
        existingUser.setUsername("ExistingUser");

        Users updatedUser = new Users();
        updatedUser.setUsername("UpdatedUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Optional<Users> result = Optional.ofNullable(userService.updateUsers(1L, updatedUser));

        assertTrue(result.isPresent());
        assertEquals("UpdatedUser", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should delete an existing user")
    public void testDeleteUser() {
        Users user = new Users();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userService.deleteUsers(1L);
        verify(userRepository, times(1)).delete(user);
    }
}