package com.CinemaGo.model.mapper;

import com.CinemaGo.model.dto.CustomUserDTO;
import com.CinemaGo.model.dto.ProfileDTO;
import com.CinemaGo.model.dto.UserDTO;
import com.CinemaGo.model.dto.UserProfileDTO;
import com.CinemaGo.model.entity.User;

public interface UserMapper {

    public UserDTO userToUserDTO(User user);

    public ProfileDTO userToProfileDTO(User user);

    public CustomUserDTO userToCustomUserDTO(User user);

    public  UserProfileDTO userToUserProfileDTO(User user);
}

