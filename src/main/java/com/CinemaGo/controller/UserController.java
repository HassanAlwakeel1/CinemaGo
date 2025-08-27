package com.CinemaGo.controller;

import com.CinemaGo.model.dto.ChangePasswordDTO;
import com.CinemaGo.model.dto.CustomUserDTO;
import com.CinemaGo.model.dto.UserDTO;
import com.CinemaGo.model.dto.UserProfileDTO;
import com.CinemaGo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserProfileDTO> updateUserProfile(@ModelAttribute UserProfileDTO userProfileDTO,
                                                                @PathVariable(value = "id") Long userId){
        return userService.updateUserProfile(userProfileDTO, userId);
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

