package com.CinemaGo.controller;

import com.CinemaGo.model.dto.*;
import com.CinemaGo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserController {
    private UserService userService;
    private static final Logger logger = Logger.getLogger(UserController.class.getName());


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<CustomUserDTO>> getAllUsers(){
        logger.info("Fetching all users");
        ResponseEntity<List<CustomUserDTO>> response = userService.getAllUsers();
        logger.info("Fetched " + (response.getBody() != null ? response.getBody().size() : 0) + " users");
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long userId){
        logger.info("Fetching user with ID: " + userId);
        ResponseEntity<UserDTO> response = userService.getUserById(userId);
        logger.info("Fetched user: " +
                (response.getBody() != null
                        ? response.getBody().getFirstName() + " " + response.getBody().getLastName()
                        : "not found"));
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @RequestBody UserProfileDTO userProfileDTO,
            @PathVariable("id") Long userId) {
        logger.info("Updating profile for user ID: " + userId);
        ResponseEntity<UserProfileDTO> response = userService.updateUserProfile(userProfileDTO, userId);
        logger.info("Updated profile for user ID: " + userId);
        return response;
    }

    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<ProfileDTO> updateProfilePicture(
            @PathVariable("id") Long userId,
            @RequestParam("file") MultipartFile file) {
        logger.info("Updating profile picture for user ID: " + userId);
        ResponseEntity<ProfileDTO> response = userService.updateProfilePicture(userId, file);
        logger.info("Updated profile picture for user ID: " + userId);
        return response;
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long userId){
        logger.info("Deleting user ID: " + userId);
        ResponseEntity<String> response = userService.deleteUser(userId);
        logger.info("Deleted user ID: " + userId);
        return response;
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable(name = "id") Long userId,
                                                 @RequestBody ChangePasswordDTO changePasswordDTO){
        logger.info("Changing password for user ID: " + userId);
        ResponseEntity<String> response = userService.changePassword(changePasswordDTO, userId);
        logger.info("Changed password for user ID: " + userId);
        return response;
    }
}

