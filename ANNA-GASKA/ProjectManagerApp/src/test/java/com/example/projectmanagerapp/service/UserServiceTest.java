package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.User;
import com.example.projectmanagerapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
        user = new User();
        user.setId(1L);
        user.setUsername("alice");
        user.setProjects(Collections.emptySet());
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("TestUser1");
        User user2 = new User();
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Nested
    @DisplayName("getUserById")
    class GetByIdTests {
        @Test
        @DisplayName("returns user when found")
        void returnsWhenFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            User result = userService.getUserById(1L);
            assertEquals("alice", result.getUsername());
            verify(userRepository).findById(1L);
        }

        @Test
        @DisplayName("throws EntityNotFoundException when not found")
        void throwsWhenNotFound() {
            when(userRepository.findById(2L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> userService.getUserById(2L));
        }
    }

    @Test
    @DisplayName("createUser should save and return user")
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        User result = userService.createUser(user);
        assertSame(user, result);
        verify(userRepository).save(user);
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateTests {
        @Test
        @DisplayName("updates existing user")
        void updatesWhenExists() {
            User updatedData = new User();
            updatedData.setUsername("bob");
            updatedData.setProjects(Set.of());

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            User result = userService.updateUser(1L, updatedData);
            assertEquals("bob", result.getUsername());
            verify(userRepository).findById(1L);
            verify(userRepository).save(result);
        }

        @Test
        @DisplayName("throws when updating non-existing user")
        void throwsWhenNotExists() {
            when(userRepository.findById(5L)).thenReturn(Optional.empty());
            assertThrows(EntityNotFoundException.class, () -> userService.updateUser(5L, user));
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteTests {
        @Test
        @DisplayName("deletes existing user")
        void deletesWhenExists() {
            when(userRepository.existsById(1L)).thenReturn(true);
            assertDoesNotThrow(() -> userService.deleteUser(1L));
            verify(userRepository).existsById(1L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("throws when deleting non-existing user")
        void throwsWhenNotExists() {
            when(userRepository.existsById(3L)).thenReturn(false);
            assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(3L));
        }
    }
}