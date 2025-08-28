package com.CinemaGo.integration.controller;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.VerificationTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VerificationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void verifyAccount_WithValidToken_ShouldEnableUser() throws Exception {
        // Arrange - create user and token
        User user = new User();
        user.setFirstName("Hassan");
        user.setLastName("Elwakeel");
        user.setEmail("hassan@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user.setEnabled(false);
        user = userRepository.save(user);

        VerificationToken token = tokenService.createToken(user);

        // Act & Assert
        mockMvc.perform(get("/api/v1/auth/verify")
                        .param("token", token.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Account verified successfully"));

        // Reload user and assert enabled
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedUser.isEnabled()).isTrue();

        // Assert token is deleted
        VerificationToken deletedToken = tokenService.getToken(token.getToken());
        assertThat(deletedToken).isNull();
    }

    @Test
    void verifyAccount_WithInvalidToken_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/auth/verify")
                        .param("token", "invalid-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid token"));
    }
}
