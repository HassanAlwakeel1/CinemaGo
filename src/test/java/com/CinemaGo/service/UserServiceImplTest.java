package com.CinemaGo.service;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.ChangePasswordDTO;
import com.CinemaGo.model.dto.CustomUserDTO;
import com.CinemaGo.model.dto.UserDTO;
import com.CinemaGo.model.dto.UserProfileDTO;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.mapper.UserMapper;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private CloudinaryImageService cloudinaryImageService;
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userMapper = mock(UserMapper.class);
        cloudinaryImageService = mock(CloudinaryImageService.class);
        userService = new UserServiceImpl(userRepository, cloudinaryImageService, passwordEncoder, userMapper);
    }

    @Test
    void getUserById_UserExists_ShouldReturnUserDTO() {
        Long userId = 1L;
        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userService.getUserById(userId);

        assertEquals(userDTO, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void getUserById_UserNotFound_ShouldThrowException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void updateUserProfile_WithPhoto_ShouldUploadAndSave() throws Exception {
        Long userId = 1L;
        User user = new User();
        UserProfileDTO dto = new UserProfileDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setBio("Bio");
        MultipartFile photo = mock(MultipartFile.class);
        dto.setProfilePicture(photo);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(photo.isEmpty()).thenReturn(false);
        when(cloudinaryImageService.upload(photo)).thenReturn(Map.of("secure_url", "url"));
        when(userMapper.userToUpdatedProfileDTO(user)).thenReturn(dto);

        ResponseEntity<UserProfileDTO> response = userService.updateUserProfile(dto, userId);

        assertEquals(dto, response.getBody());
        verify(userRepository).save(user);
        assertEquals("John", user.getFirstName());
        assertEquals("url", user.getProfilePictureURL());
    }

    @Test
    void deleteUser_UserExists_ShouldDelete() {
        Long userId = 1L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userService.deleteUser(userId);

        verify(userRepository).delete(user);
        assertEquals("User deleted successfully", response.getBody());
    }

    @Test
    void getAllUsers_ShouldReturnList() {
        User user1 = new User();
        User user2 = new User();
        CustomUserDTO dto1 = new CustomUserDTO();
        CustomUserDTO dto2 = new CustomUserDTO();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(userMapper.userToCustomUserDTO(user1)).thenReturn(dto1);
        when(userMapper.userToCustomUserDTO(user2)).thenReturn(dto2);

        ResponseEntity<List<CustomUserDTO>> response = userService.getAllUsers();

        assertEquals(2, response.getBody().size());
        assertEquals(dto1, response.getBody().get(0));
        assertEquals(dto2, response.getBody().get(1));
    }

    @Test
    void changePassword_Valid_ShouldUpdatePassword() {
        Long userId = 1L;
        User user = new User();
        user.setPassword("encodedOld");
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setOldPassword("old");
        dto.setNewPassword("new");
        dto.setNewPasswordConfirmation("new");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encodedOld")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encodedNew");

        ResponseEntity<String> response = userService.changePassword(dto, userId);

        verify(userRepository).save(user);
        assertEquals("encodedNew", user.getPassword());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password changed successfully!", response.getBody());
    }

    @Test
    void changePassword_Invalid_ShouldReturnBadRequest() {
        Long userId = 1L;
        User user = new User();
        user.setPassword("encodedOld");
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setOldPassword("wrong");
        dto.setNewPassword("new");
        dto.setNewPasswordConfirmation("new");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedOld")).thenReturn(false);

        ResponseEntity<String> response = userService.changePassword(dto, userId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad credentials", response.getBody());
        verify(userRepository, never()).save(any());
    }
}
