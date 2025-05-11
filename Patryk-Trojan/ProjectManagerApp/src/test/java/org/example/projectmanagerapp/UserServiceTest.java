package org.example.projectmanagerapp;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository= Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }
    @Test
    @DisplayName("Should return all users")
    public void shouldReturnAllUsers() {
        User user = new User();
        user.setUsername("testUser");

        User user2 = new User();
        user.setUsername("testUser2");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));
        List<User> users = userService.getAllUsers();
        assertEquals(2,users.size()) ;
        verify(userRepository,times(1)).findAll();
    }
    @Test
    @DisplayName("Should delete all users")
    public void shouldDeleteAllUsers() {
        // Given
        User user1 = new User();
        user1.setUsername("testUser1");

        User user2 = new User();
        user2.setUsername("testUser2");


        when(userRepository.findAll())
                .thenReturn(Arrays.asList(user1, user2))
                .thenReturn(Collections.emptyList());

        doNothing().when(userRepository).deleteAll();


        List<User> initialUsers = userService.getAllUsers();


        userService.deleteAllUsers();


        List<User> usersAfterDeletion = userService.getAllUsers();

        // Then
        assertEquals(2, initialUsers.size());
        assertEquals(0, usersAfterDeletion.size());

        verify(userRepository, times(2)).findAll();
        verify(userRepository, times(1)).deleteAll();
    }
    @Test
    @DisplayName("Should return 1 user")
    public void shouldReturnUserByID() {
        //given
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User temp=userService.findUserById(1L);
        assertEquals(user,temp) ;
        verify(userRepository,times(1)).findById(1L);
    }
    @Test
    @DisplayName("Should delete user by ID")
    public void shouldDeleteUserByID() {


        User user = new User();
        user.setUsername("testUser");


        userService.deleteUser(1L);

        List<User> users = userService.getAllUsers();
        assertEquals(0,users.size()) ;


    }
    @Test
    @DisplayName("Should throw exception when updating non-existent user")
    public void shouldThrowWhenUpdatingNonExistentUser() {
        // Given
        Long nonExistentUserId = 99L;
        User updatedUserData = new User();
        updatedUserData.setUsername("patryk");

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(nonExistentUserId, updatedUserData);
        });

        verify(userRepository, never()).save(any());
    }
    @Test
    @DisplayName("Should create a user")
    void shouldCreateUser() {
        // przygotuj obiekt zwracany przez save()
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testUser");


        User inputUser = new User();
        inputUser.setUsername("testUser");


        when(userRepository.save(inputUser)).thenReturn(savedUser);


        when(userRepository.findAll()).thenReturn(List.of(savedUser));

        userService.createUser(inputUser);
        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
        verify(userRepository, times(1)).save(inputUser);
        verify(userRepository, times(1)).findAll();
    }
    @Test
    @DisplayName("Should update a user")
    void shouldUpdateUser() {
        // Given
        Long userId = 1L;

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        User inputUser = new User();
        inputUser.setUsername("newUsername");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("newUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);

        // When
        userService.updateUser(userId, inputUser);

        // Then
        assertEquals("newUsername", existingUser.getUsername());
        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }



}
