package com.CinemaGo.service;

import com.CinemaGo.model.dto.ChangePasswordDTO;
import com.CinemaGo.model.dto.CustomUserDTO;
import com.CinemaGo.model.dto.UserDTO;
import com.CinemaGo.model.dto.UserProfileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDetailsService userDetailsService();
    ResponseEntity<UserDTO> getUserById(Long userId);

    ResponseEntity<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId);

    ResponseEntity<String> deleteUser(Long userId);

    ResponseEntity<List<CustomUserDTO>> getAllUsers();

    ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId);
}
