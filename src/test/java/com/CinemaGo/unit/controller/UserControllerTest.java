package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.UserController;
import com.CinemaGo.model.dto.*;
import com.CinemaGo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<CustomUserDTO> mockUsers = Arrays.asList(new CustomUserDTO(), new CustomUserDTO());
        ResponseEntity<List<CustomUserDTO>> mockResponse = ResponseEntity.ok(mockUsers);

        when(userService.getAllUsers()).thenReturn(mockResponse);

        ResponseEntity<List<CustomUserDTO>> response = userController.getAllUsers();

        assertEquals(mockResponse, response);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testFindUserById() {
        Long userId = 1L;
        UserDTO mockUser = new UserDTO();
        ResponseEntity<UserDTO> mockResponse = ResponseEntity.ok(mockUser);

        when(userService.getUserById(userId)).thenReturn(mockResponse);

        ResponseEntity<UserDTO> response = userController.findUserById(userId);

        assertEquals(mockResponse, response);
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testUpdateUserProfile() {
        Long userId = 1L;
        UserProfileDTO profileDTO = new UserProfileDTO();
        ResponseEntity<UserProfileDTO> mockResponse = ResponseEntity.ok(profileDTO);

        when(userService.updateUserProfile(profileDTO, userId)).thenReturn(mockResponse);

        ResponseEntity<UserProfileDTO> response = userController.updateUserProfile(profileDTO, userId);

        assertEquals(mockResponse, response);
        verify(userService, times(1)).updateUserProfile(profileDTO, userId);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;
        ResponseEntity<String> mockResponse = ResponseEntity.ok("User deleted");

        when(userService.deleteUser(userId)).thenReturn(mockResponse);

        ResponseEntity<String> response = userController.deleteUser(userId);

        assertEquals(mockResponse, response);
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testChangePassword() {
        Long userId = 1L;
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Password changed");

        when(userService.changePassword(changePasswordDTO, userId)).thenReturn(mockResponse);

        ResponseEntity<String> response = userController.changePassword(userId, changePasswordDTO);

        assertEquals(mockResponse, response);
        verify(userService, times(1)).changePassword(changePasswordDTO, userId);
    }
}
