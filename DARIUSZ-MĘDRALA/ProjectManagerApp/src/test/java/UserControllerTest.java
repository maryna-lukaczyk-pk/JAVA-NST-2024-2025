import org.example.projectmanagerapp.controller.UserController;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    @DisplayName("Should return all users")
    void findAll_shouldReturnAllUsers() {

        User user1 = new User();
        user1.setUsername("Dariusz");

        User user2 = new User();
        user2.setUsername("Michał");

        List<User> expectedUsers = Arrays.asList(user1, user2);

        when(userService.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userController.getUsers();


        assertEquals(expectedUsers.size(), actualUsers.size());
        assertEquals(expectedUsers, actualUsers);
        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create a new user and created status")
    void createUser_shouldCreateNewUserAndCreatedStatusWithUser() {

        User user = new User();
        user.setUsername("Dariusz");

        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<User> response= userController.addUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    @DisplayName("Should delete a user and no content status")
    void deleteUser_shouldDeleteUserAndNoContentStatus() {

        Long userId = 1L;

        ResponseEntity<Void> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("Should update a user and ok status")
    void updateUser_shouldUpdateUserAndOkStatus() {

        Long userId = 1L;
        User user = new User();
        user.setUsername("Dariusz");

        when(userService.updateUser(userId, user)).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(userId, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).updateUser(userId, user);
    }

    @Test
    @DisplayName("Should return a user by ID and ok status")
    void getUserById_shouldReturnUserAndOkStatus() {

        Long userId = 1L;
        User user = new User();
        user.setUsername("Dariusz");

        when(userService.findById(userId)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).findById(userId);
    }
}
