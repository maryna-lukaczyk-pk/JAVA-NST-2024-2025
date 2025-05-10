package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.Users;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService();

        // Use reflection to set the repository
        try {
            java.lang.reflect.Field field = UserService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            field.set(userService, userRepository);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Should return all users")
    void testGetAllUsers() {
        // Arrange
        Users user1 = new Users();
        user1.setUser_name("TestUser1");

        Users user2 = new Users();
        user2.setUser_name("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<Users> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    void testGetUserById() {
        // Arrange
        Users user = new Users();
        user.setUser_id(1L);
        user.setUser_name("TestUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        Optional<Users> result = userService.getUserById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("TestUser", result.get().getUser_name());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create user")
    void testCreateUser() {
        // Arrange
        Users user = new Users();
        user.setUser_name("NewUser");

        when(userRepository.save(any(Users.class))).thenReturn(user);

        // Act
        Users result = userService.createUser(user);

        // Assert
        assertEquals("NewUser", result.getUser_name());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update user name only")
    void testUpdateUserNameOnly() {
        // Arrange
        Users existingUser = new Users();
        existingUser.setUser_id(1L);
        existingUser.setUser_name("OldName");

        Users updatedDetails = new Users();
        updatedDetails.setUser_name("NewName");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Users result = userService.updateUser(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("NewName", result.getUser_name());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should update user projects only")
    void testUpdateUserProjectsOnly() {
        // Arrange
        Users existingUser = new Users();
        existingUser.setUser_id(1L);
        existingUser.setUser_name("OldName");

        Users updatedDetails = new Users();
        updatedDetails.setProjects(new java.util.HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Users result = userService.updateUser(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("OldName", result.getUser_name());
        assertNotNull(result.getProjects());
        assertTrue(result.getProjects().isEmpty());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should update both user name and projects")
    void testUpdateUserNameAndProjects() {
        // Arrange
        Users existingUser = new Users();
        existingUser.setUser_id(1L);
        existingUser.setUser_name("OldName");

        Users updatedDetails = new Users();
        updatedDetails.setUser_name("NewName");
        updatedDetails.setProjects(new java.util.HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Users result = userService.updateUser(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("NewName", result.getUser_name());
        assertNotNull(result.getProjects());
        assertTrue(result.getProjects().isEmpty());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("Should return null when updating non-existent user")
    void testUpdateNonExistentUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Users result = userService.updateUser(1L, new Users());

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("Should delete user")
    void testDeleteUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent user")
    void testDeleteNonExistentUser() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = userService.deleteUser(1L);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(any());
    }
}
