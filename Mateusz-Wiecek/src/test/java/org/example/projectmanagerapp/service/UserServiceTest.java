package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    private User user1, user2;

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "alice");
        user2 = new User(2L, "bob");
    }

    @Test @DisplayName("getAllUsers returns all users")
    void getAllUsers() {
        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user1, user2));
        assertEquals(2, userService.getAllUsers().size());
    }

    @Test @DisplayName("getUserById returns user when exists")
    void getUserByIdExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        Optional<User> u = userService.getUserById(1L);
        assertTrue(u.isPresent());
        assertEquals("alice", u.get().getUsername());
    }

    @Test @DisplayName("getUserById returns empty when not exists")
    void getUserByIdNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(userService.getUserById(99L).isPresent());
    }

    @Test @DisplayName("createUser saves the user")
    void createUser() {
        when(userRepository.save(user1)).thenReturn(user1);
        assertEquals("alice", userService.createUser(user1).getUsername());
    }

    @Test @DisplayName("updateUser updates when exists")
    void updateUserExists() {
        User updates = new User(null, "aliceX");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        Optional<User> up = userService.updateUser(1L, updates);
        assertTrue(up.isPresent());
        assertEquals("aliceX", up.get().getUsername());
    }

    @Test @DisplayName("updateUser returns empty when not exists")
    void updateUserNotExists() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertFalse(userService.updateUser(99L, user1).isPresent());
    }

    @Test @DisplayName("deleteUser returns true when it existed")
    void deleteUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        assertTrue(userService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test @DisplayName("deleteUser returns false when it did not exist")
    void deleteUserNotExists() {
        when(userRepository.existsById(99L)).thenReturn(false);
        assertFalse(userService.deleteUser(99L));
        verify(userRepository, never()).deleteById(anyLong());
    }
}
