package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.schemas.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class UserServiceTest {
    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void testFindAllUsers() {
        User user1 = new User();
        user1.setUsername("Adam");

        User user2 = new User();
        user2.setUsername("Jan");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user by ID")
    void testFindUserById() throws NotFoundException {
        User user = new User();
        user.setUsername("Adam");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1);

        assertEquals("Adam", result.getUsername());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Should add a new user")
    void testAddUser() {
        UserDTO dto = new UserDTO();
        dto.setUsername("NowyUser");

        userService.addUser(dto);

        Mockito.verify(userRepository, times(1))
                .save(argThat(user ->
                    user.getUsername().equals("NowyUser")
        ));
    }

    @Test
    @DisplayName("Should delete user when ID exists")
    void testDeleteUser_whenExists() throws NotFoundException {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existing user")
    void testDeleteUser_whenNotExists() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1));
    }

    @Test
    @DisplayName("Should update existing user")
    void testUpdateUser_WhenExists() throws NotFoundException {
        when(userRepository.existsById(1)).thenReturn(true);

        UserDTO dto = new UserDTO();
        dto.setUsername("UpdatedUser");

        userService.updateUser(dto, 1);

        verify(userRepository, times(1))
                .save(argThat(user ->
                        user.getId() == 1 && user.getUsername().equals("UpdatedUser")
                ));
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existing user")
    void testUpdateUser_whenNotExists() {
        when(userRepository.existsById(1)).thenReturn(false);

        UserDTO dto = new UserDTO();
        dto.setUsername("UserNotExist");

        assertThrows(NotFoundException.class, () -> userService.updateUser(dto, 1));
    }
}
