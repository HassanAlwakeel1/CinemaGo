package com.CinemaGo.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePictureUrl;
}
