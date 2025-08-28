package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.UserProfileDTO;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.CloudinaryImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;


import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CloudinaryImageService cloudinaryImageService;



    @Test
    void testGetUserById_ShouldReturnUser() throws Exception {
        // Arrange - save a test user
        User user = new User();
        user.setFirstName("Hassan");
        user.setLastName("Elwakeel");
        user.setEmail("hassan@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        // Act + Assert
        mockMvc.perform(get("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value("Hassan"))
                .andExpect(jsonPath("$.lastName").value("Elwakeel"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void testGetAllUsers_ShouldReturnListOfUsers() throws Exception {
        // Arrange - save 2 users
        User user1 = new User();
        user1.setFirstName("Hassan");
        user1.setLastName("Elwakeel");
        user1.setEmail("hassan1@test.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRole(Role.USER);
        userRepository.save(user1);

        User user2 = new User();
        user2.setFirstName("Omar");
        user2.setLastName("Ali");
        user2.setEmail("omar@test.com");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setRole(Role.ADMIN);
        userRepository.save(user2);

        // Act + Assert
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Hassan"))
                .andExpect(jsonPath("$[1].firstName").value("Omar"));
    }

    @Test
    void testUpdateUserProfile_ShouldUpdateJsonFields() throws Exception {
        // Arrange - save a test user
        User user = new User();
        user.setFirstName("Hassan");
        user.setLastName("Elwakeel");
        user.setEmail("hassan_update@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole(Role.USER);
        user = userRepository.save(user);

        // Prepare update payload
        UserProfileDTO updatedProfile = new UserProfileDTO();
        updatedProfile.setFirstName("HassanUpdated");
        updatedProfile.setLastName("ElwakeelUpdated");
        updatedProfile.setBio("New Bio");

        // Act + Assert
        mockMvc.perform(put("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProfile)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().intValue()))
                .andExpect(jsonPath("$.firstName").value("HassanUpdated"))
                .andExpect(jsonPath("$.lastName").value("ElwakeelUpdated"))
                .andExpect(jsonPath("$.bio").value("New Bio"));
    }
}
