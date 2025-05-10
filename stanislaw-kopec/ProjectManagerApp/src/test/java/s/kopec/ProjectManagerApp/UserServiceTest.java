package s.kopec.ProjectManagerApp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import s.kopec.ProjectManagerApp.entity.User;
import s.kopec.ProjectManagerApp.repository.UserRepository;
import s.kopec.ProjectManagerApp.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
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
        user1.setUsername("TestUser1");

        User user2 = new User();
        user2.setUsername("TestUser2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1,user2));

        List<User> users = userService.getAllUsers();

        assertEquals(2,users.size());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    @DisplayName("Should return user if user exist")
    void testFindUserById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test_user");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("test_user", result.get().getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        User newUser = new User();
        newUser.setUsername("new_user");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User createUser = userService.createUser(newUser);

        assertNotNull(createUser);
        assertEquals("new_user", createUser.getUsername());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    @DisplayName("Should delete an existing user")
    public void testDeleteUserById() {
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should update an existing user's username")
    public void testUpdateUserName() {
        Long userId = 1L;
        String newUsername = "updated_user";

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("old_user");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.getReferenceById(userId)).thenReturn(existingUser);
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        userService.updateUserName(userId, newUsername);

        assertEquals(newUsername, existingUser.getUsername());
        verify(userRepository).existsById(userId);
        verify(userRepository).getReferenceById(userId);
        verify(userRepository).save(existingUser);
    }

}
