import org.example.projectmanagerapp.controller.UserController;
import org.example.projectmanagerapp.entity.User;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

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
    void testGetAllUsers() {
        User user1 = new User();
        user1.setUsername("Darek");

        User user2 = new User();
        user2.setUsername("Jerzy");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userController.all();

        assertEquals(2, users.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Should create a new user")
    void testCreateUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Darek");

        when(userService.createUser(user)).thenReturn(user);

        User createdUser = userController.newUser(user);
        assertEquals(user, createdUser);

        verify(userService, times(1)).createUser(user);
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

        when(userService.updateById(1L, user2)).thenReturn(user2);

        var user1Updated = userController.updateUser(1L, user2);

        assertEquals(user2, user1Updated.getBody());
        assertEquals(HttpStatus.OK, user1Updated.getStatusCode());
        verify(userService, times(1)).updateById(1L, user2);
    }

    @Test
    @DisplayName("Should delete a chosen user")
    void testDeleteUser() {
        when(userService.deleteById(1L)).thenReturn(true);

        var deleteResult = userController.deleteUser(1L);

        assertEquals(HttpStatus.OK, deleteResult.getStatusCode());

        when(userService.deleteById(1L)).thenReturn(false);

        deleteResult = userController.deleteUser(1L);

        assertEquals(HttpStatus.BAD_REQUEST, deleteResult.getStatusCode());

        verify(userService, times(2)).deleteById(1L);
    }

    @Test
    @DisplayName("Should return chosen user")
    void testGetUserById() {
        User user = new User();
        user.setUsername("Darek");
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(user);

        var userRetrieveResult = userController.getUserById(1L);

        assertEquals(user, userRetrieveResult.getBody());
        assertEquals(HttpStatus.OK, userRetrieveResult.getStatusCode());

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    @DisplayName("Should throw exception on missing user")
    void testGetUserByIdException() {
        when(userService.getUserById(1L)).thenThrow(new RuntimeException("User does not exist"));

        var userRetrieveResult = userController.getUserById(1L);

        assertEquals(HttpStatus.BAD_REQUEST, userRetrieveResult.getStatusCode());

        verify(userService, times(1)).getUserById(1L);
    }
}
