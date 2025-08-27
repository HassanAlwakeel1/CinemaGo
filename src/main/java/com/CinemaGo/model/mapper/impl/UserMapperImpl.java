package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.CustomUserDTO;
import com.CinemaGo.model.dto.ProfileDTO;
import com.CinemaGo.model.dto.UserDTO;
import com.CinemaGo.model.dto.UserProfileDTO;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.mapper.UserMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {
    private ModelMapper mapper;

    public UserMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public UserDTO userToUserDTO(User user){
        return mapper.map(user,UserDTO.class);
    }

    @Override
    public UserProfileDTO userToUpdatedProfileDTO(User user) {
        return mapper.map(user,UserProfileDTO.class);
    }

    @Override
    public CustomUserDTO userToCustomUserDTO(User user){
        return mapper.map(user,CustomUserDTO.class);
    }

    @Override
    public ProfileDTO userToProfileDTO(User user) {
        return mapper.map(user,ProfileDTO.class);
    }


}