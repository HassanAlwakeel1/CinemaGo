package com.CinemaGo.model.dto;

import com.CinemaGo.model.entity.enums.Role;
import lombok.Data;

@Data
public class SignUpRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String bio;

    private Role role;
}