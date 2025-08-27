package com.CinemaGo.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String Bio;
    private MultipartFile profilePicture;
    //Here I didn't put password and email beacuse they will have another way to handle
}

