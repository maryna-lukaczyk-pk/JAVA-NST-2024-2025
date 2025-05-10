import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("Should return all users")
    void findAll_shouldReturnAllUsers() {

        User user1 = new User();
        user1.setUsername("Dariusz");

        User user2 = new User();
        user2.setUsername("Michał");

        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.findAll();

        assertEquals(expectedUsers.size(), actualUsers.size());
        assertTrue(actualUsers.contains(user1));
        assertTrue(actualUsers.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new user")
    void createUser_shouldCreateNewUser() {

        User user = new User();
        user.setUsername("Dariusz");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertEquals(user, createdUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should delete a user")
    void deleteUser_shouldDeleteUser() {

        User user = new User();
        user.setId(1L);
        user.setUsername("Dariusz");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User deletedUser = userService.deleteUser(1L);

        assertEquals(user, deletedUser);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("Should update an user")
    void updateUser_shouldUpdateUser() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("Dariusz");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("Michał");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateUser(1L, updatedUser);

        assertEquals(updatedUser, result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    @DisplayName("Should find a user by ID")
    void findById_shouldReturnUser() {

        User user = new User();
        user.setId(1L);
        user.setUsername("Dariusz");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void findById_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.findById(1L);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing user")
    void deleteUser_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.deleteUser(1L);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existing user")
    void updateUser_shouldThrowExceptionWhenUserNotFound() {

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("Dariusz");

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.updateUser(1L, updatedUser);
        } catch (RuntimeException e) {
            assertEquals("User not found", e.getMessage());
        }

        verify(userRepository, times(1)).findById(1L);
    }
}
