package com.CinemaGo.service.impl;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.*;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.mapper.UserMapper;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.CloudinaryImageService;
import com.CinemaGo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private CloudinaryImageService cloudinaryImageService;

    private PasswordEncoder passwordEncoder;

    private UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           CloudinaryImageService cloudinaryImageService,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.cloudinaryImageService = cloudinaryImageService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserDetailsService userDetailsService(){
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                LOGGER.info("Loading user by email: {}", email);
                return userRepository.findByEmail(email)
                        .orElseThrow(()-> new UsernameNotFoundException("User not found"));
            }
        };
    }


    public ResponseEntity<UserDTO> getUserById(Long userId){
        LOGGER.info("Getting user by id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        UserDTO userDTO =  userMapper.userToUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @Override
    public ResponseEntity<UserProfileDTO> updateUserProfile(UserProfileDTO userProfileDTO, Long userId) {
        LOGGER.info("Updating user profile for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setFirstName(userProfileDTO.getFirstName());
        user.setLastName(userProfileDTO.getLastName());
        user.setBio(userProfileDTO.getBio());

        userRepository.save(user);

        UserProfileDTO updatedUserProfileDTO = userMapper.userToUserProfileDTO(user);
        return ResponseEntity.ok(updatedUserProfileDTO);
    }

    @Override
    public ResponseEntity<ProfileDTO> updateProfilePicture(Long userId, MultipartFile photo) {
        LOGGER.info("Updating profile picture for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (photo != null && !photo.isEmpty()) {
            Map uploadImageMap = cloudinaryImageService.upload(photo);
            String photoUrl = (String) uploadImageMap.get("secure_url");
            user.setProfilePictureURL(photoUrl);
        }

        userRepository.save(user);

        ProfileDTO updatedProfileDTO = userMapper.userToProfileDTO(user);
        return ResponseEntity.ok(updatedProfileDTO);
    }

    @Override
    public ResponseEntity<String> deleteUser(Long userId) {
        LOGGER.info("Deleting user with id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }

    @Override
    public ResponseEntity<List<CustomUserDTO>> getAllUsers() {
        LOGGER.info("Getting all users");
        List<User> users = userRepository.findAll();
        List<CustomUserDTO> customUserDTOS = new ArrayList<>();
        for (User user : users) {
            CustomUserDTO customUserDTO = userMapper.userToCustomUserDTO(user);
            customUserDTOS.add(customUserDTO);
        }
        return ResponseEntity.ok(customUserDTOS);
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordDTO changePasswordDTO, Long userId) {
        LOGGER.info("Changing password for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",userId));
        String userPassword = user.getPassword();
        String oldPassword = changePasswordDTO.getOldPassword();
        String newPassword = changePasswordDTO.getNewPassword();
        String newPasswordConfirmation = changePasswordDTO.getNewPasswordConfirmation();
        if (newPassword.equals(newPasswordConfirmation) && passwordEncoder.matches(oldPassword, userPassword)){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.ok("Password changed successfully!");
        }else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad credentials");
    }
}