package com.CinemaGo.controller;

import com.CinemaGo.model.dto.*;
import com.CinemaGo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserController {
    private UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<CustomUserDTO>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable(value = "id") Long userId){
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDTO> updateUserProfile(
            @RequestBody UserProfileDTO userProfileDTO,
            @PathVariable("id") Long userId) {
        return userService.updateUserProfile(userProfileDTO, userId);
    }

    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<ProfileDTO> updateProfilePicture(
            @PathVariable("id") Long userId,
            @RequestParam("file") MultipartFile file) {
        return userService.updateProfilePicture(userId, file);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long userId){
        return userService.deleteUser(userId);
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<String> changePassword(@PathVariable(name = "id") Long userId,
                                                 @RequestBody ChangePasswordDTO changePasswordDTO){
        return userService.changePassword(changePasswordDTO, userId);
    }


}

