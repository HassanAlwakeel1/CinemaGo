package com.CinemaGo.controller;

import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.VerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationControllerTest {

    @Mock
    private VerificationTokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VerificationController verificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyAccount_Success() {
        String tokenString = "valid-token";
        User mockUser = new User();
        mockUser.setEnabled(false);

        VerificationToken mockToken = new VerificationToken();
        mockToken.setUser(mockUser);

        when(tokenService.getToken(tokenString)).thenReturn(mockToken);
        doNothing().when(tokenService).deleteToken(mockToken);
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        ResponseEntity<String> response = verificationController.verifyAccount(tokenString);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Account verified successfully", response.getBody());
        assertTrue(mockUser.isEnabled());

        verify(tokenService, times(1)).getToken(tokenString);
        verify(userRepository, times(1)).save(mockUser);
        verify(tokenService, times(1)).deleteToken(mockToken);
    }

    @Test
    void testVerifyAccount_InvalidToken() {
        String tokenString = "invalid-token";

        when(tokenService.getToken(tokenString)).thenReturn(null);

        ResponseEntity<String> response = verificationController.verifyAccount(tokenString);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid token", response.getBody());

        verify(tokenService, times(1)).getToken(tokenString);
        verifyNoInteractions(userRepository);
    }
}
