package com.example.projectmanagerapp.service;

import com.example.projectmanagerapp.entity.Project;
import com.example.projectmanagerapp.entity.Users;
import com.example.projectmanagerapp.repozytorium.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UsersRepository usersRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        usersRepository = mock(UsersRepository.class);
        userService = new UserService(usersRepository);
    }

    @Test
    void testGetUserByIdSuccess() {
        Users user = new Users();
        user.setId(1L);
        user.setUsername("testuser");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

        Users result = userService.getUserById(1L);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testGetUserByIdNotFound() {
        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testCreateUser() {
        Users user = new Users();
        user.setUsername("newuser");

        Users savedUser = new Users();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");

        when(usersRepository.save(user)).thenReturn(savedUser);

        Users result = userService.createUser(user);
        assertNotNull(result.getId());
        assertEquals("newuser", result.getUsername());
    }

    @Test
    void testUpdateUserSuccess() {
        Users existingUser = new Users();
        existingUser.setId(1L);
        existingUser.setUsername("oldname");

        Users updatedDetails = new Users();
        updatedDetails.setUsername("newname");

        when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Users result = userService.updateUser(1L, updatedDetails);
        assertEquals("newname", result.getUsername());
    }

    @Test
    void testUpdateUserNotFound() {
        Users updatedDetails = new Users();
        updatedDetails.setUsername("newname");

        when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.updateUser(1L, updatedDetails));
    }

    @Test
    void testDeleteUserSuccess() {
        when(usersRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(usersRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(usersRepository.existsById(1L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(1L));
    }
    @Test
    void testSetAndGetProjectSet() {
        Users user = new Users();

        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Project A");

        Project project2 = new Project();
        project2.setId(2L);
        project2.setName("Project B");

        Set<Project> projects = new HashSet<>();
        projects.add(project1);
        projects.add(project2);

        user.setProjectSet(projects);
        Set<Project> result = user.getProjectSet();

        assertEquals(2, result.size());
        assertTrue(result.contains(project1));
        assertTrue(result.contains(project2));
    }
}

