package com.example.projectmanagerapp.service;


import com.example.projectmanagerapp.entity.users;
import com.example.projectmanagerapp.repository.users_repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class user_test {

    @Mock
    private users_repository users_repository;

    @InjectMocks
    private user_service user_service;

    private users user1;
    private users user2;

    @BeforeEach
    void setUp() {
        user1 = new users();
        user1.setId(1L);
        user1.setUsername("TestUser1");

        user2 = new users();
        user2.setId(2L);
        user2.setUsername("TestUser2");
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        when(users_repository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<users> result = user_service.getAllUsers();

        assertEquals(2, result.size());
        verify(users_repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a user")
    void testCreateUser() {
        when(users_repository.save(any(users.class))).thenReturn(user1);

        users result = user_service.create_user(user1);

        assertNotNull(result);
        assertEquals(user1.getUsername(), result.getUsername());
        verify(users_repository, times(1)).save(any(users.class));
    }

    @Test
    @DisplayName("Should find user by ID")
    void testGetUserById() {
        when(users_repository.findById(1L)).thenReturn(Optional.of(user1));

        Optional<users> result = user_service.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user1.getUsername(), result.get().getUsername());
        verify(users_repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void testGetUserByIdNotFound() {
        when(users_repository.findById(3L)).thenReturn(Optional.empty());

        Optional<users> result = user_service.getUserById(3L);

        assertFalse(result.isPresent());
        verify(users_repository, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Should delete user by ID")
    void testDeleteUserById() {
        Long userId = 1L;
        doNothing().when(users_repository).deleteById(userId);

        user_service.deleteUserById(userId);

        verify(users_repository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should update user")
    void testUpdateUser() {
        Long userId = 1L;
        users updatedUser = new users();
        updatedUser.setUsername("UpdatedUsername");

        when(users_repository.findById(userId)).thenReturn(Optional.of(user1));
        when(users_repository.save(any(users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        users result = user_service.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals("UpdatedUsername", result.getUsername());
        verify(users_repository, times(1)).findById(userId);
        verify(users_repository, times(1)).save(any(users.class));
    }


}