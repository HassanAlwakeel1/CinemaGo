package com.CinemaGo.model.dto;

import lombok.Data;

@Data
public class PersonRequestDTO {
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
}
