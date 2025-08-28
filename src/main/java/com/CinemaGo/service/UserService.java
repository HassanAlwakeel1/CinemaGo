package com.CinemaGo.service;

import com.CinemaGo.model.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    ResponseEntity<UserDTO> getUserById(Long userId);

    ResponseEntity<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId);

    ResponseEntity<String> deleteUser(Long userId);

    ResponseEntity<List<CustomUserDTO>> getAllUsers();

    ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId);
    ResponseEntity<ProfileDTO> updateProfilePicture(Long userId, MultipartFile photo);
}
