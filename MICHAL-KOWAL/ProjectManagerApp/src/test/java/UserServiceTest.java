import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("Darek");

        User user2 = new User();
        user2.setUsername("Jerzy");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return chosen user")
    void testGetUserById() {
        User user = new User();
        user.setUsername("Darek");
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User userRetrieved = userService.getUserById(1L);

        assertEquals(user, userRetrieved);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception on missing user")
    void testGetUserByIdException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            userService.getUserById(1L);
        } catch (RuntimeException e) {
            assertEquals("User does not exist", e.getMessage());
        }

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        User user = new User();
        user.setUsername("Darek");

        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);
        assertEquals(user, createdUser);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Should update a chosen user")
    void testUpdateUser() {
        User user1 = new User();
        user1.setUsername("Darek");
        user1.setId(1L);

        User user2 = new User();
        user2.setUsername("Jerzy");
        user2.setId(1L);

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(user2)).thenReturn(user2);

        User user1Updated = userService.updateUserById(1L, user2);

        assertEquals(user2, user1Updated);
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).save(user2);
    }

    @Test
    @DisplayName("Should delete a chosen user")
    void testDeleteUser() {
        User user1 = new User();
        user1.setUsername("Darek");
        user1.setId(1L);

        when(userRepository.existsById(1L)).thenReturn(true);

        Boolean deleted = userService.deleteUserById(1L);

        assertEquals(true, deleted);

        when(userRepository.existsById(1L)).thenReturn(false);

        deleted = userService.deleteUserById(1L);

        assertEquals(false, deleted);

        verify(userRepository, times(2)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
