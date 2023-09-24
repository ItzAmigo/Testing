package com.Itzamigo.SQATProject.controller;

import com.Itzamigo.SQATProject.entity.User;
import com.Itzamigo.SQATProject.exception.UserNotFoundException;
import com.Itzamigo.SQATProject.repository.UserRepository;
import com.Itzamigo.SQATProject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void testGetUsersList() {
        User user1 = new User(8L, "n2", "e2");
        User user2 = new User(9L, "n3", "e3");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<User> userList = userService.getUsers();
        assertEquals(userList.size(), 2);
        assertEquals(userList.get(0).getNickname(), "n2");
        assertEquals(userList.get(1).getNickname(), "n3");
        assertEquals(userList.get(0).getEmail(), "e3");
        assertEquals(userList.get(1).getEmail(), "e3");
    }

    @Test
    public void testGetUserById() {
        User user = new User(7L, "n4", "e4");
        when(userRepository.findById(7L)).thenReturn(Optional.of(user));
        User userById = userService.getUserById(7L);
        assertNotEquals(userById, null);
        assertEquals(userById.getNickname(), "n4");
        assertEquals(userById.getEmail(), "e4");
    }


    @Test
    public void testGetInvalidUserById() {
        when(userRepository.findById(17L)).thenThrow(new UserNotFoundException("User Not Found with ID"));
        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(17L);
        });
        assertTrue(exception.getMessage().contains("User Not Found with ID"));
    }

    @Test
    public void testCreateUser() {
        User user = new User(12L, "n5", "e5");
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User userCreated = userArgumentCaptor.getValue();
        assertNotNull(userCreated.getId());
        assertEquals("n5", userCreated.getNickname());
    }
    @Test
    public void testDeleteUser() {
        User user = new User(13L, "n5", "e5");
        when(userRepository.findById(13L)).thenReturn(Optional.of(user));
        userService.deleteUserById(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
        ArgumentCaptor<Long> userArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(userRepository).deleteById(userArgumentCaptor.capture());
        Long userIdDeleted = userArgumentCaptor.getValue();
        assertNotNull(userIdDeleted);
        assertEquals(13L, userIdDeleted);
    }
}
