package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.*;
import com.CinemaGo.model.entity.PasswordResetToken;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.PasswordResetTokenRepository;
import com.CinemaGo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        passwordResetTokenRepository.deleteAll();
    }

    @Test
    void testSignup_ShouldReturnJwtTokens() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setFirstName("Hassan");
        request.setLastName("Alwakeel");
        request.setEmail("signup@test.com");
        request.setPassword("secure123");
        request.setBio("Test bio");
        request.setRole(Role.USER);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void testSignin_ShouldReturnJwtTokens() throws Exception {
        // Arrange: insert user manually into DB
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("signin@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

        SignInRequest request = new SignInRequest();
        request.setEmail("signin@test.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void testForgotPassword_ShouldReturnSuccess() throws Exception {
        User user = new User();
        user.setFirstName("Reset");
        user.setLastName("User");
        user.setEmail("reset@test.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .param("email", "reset@test.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password reset link has been sent to your email"));
    }

    @Test
    void testResetPassword_ShouldReturnSuccess() throws Exception {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("reset2@test.com");
        user.setPassword(passwordEncoder.encode("oldpass"));
        user.setRole(Role.USER);
        user.setEnabled(true);
        userRepository.save(user);

        // Create token manually
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken("valid-token");
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(token);

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("valid-token");
        request.setNewPassword("newpass123");

        mockMvc.perform(put("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password has been reset successfully"));
    }
}
