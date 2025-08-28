package com.CinemaGo.controller;

import com.CinemaGo.model.dto.*;
import com.CinemaGo.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignup() {
        SignUpRequest signUpRequest = new SignUpRequest();
        JwtAuthenticationResponse mockResponse = new JwtAuthenticationResponse("accessToken", "refreshToken");

        when(authenticationService.signup(signUpRequest)).thenReturn(mockResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signup(signUpRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(authenticationService, times(1)).signup(signUpRequest);
    }

    @Test
    void testSignin() {
        SignInRequest signInRequest = new SignInRequest();
        JwtAuthenticationResponse mockResponse = new JwtAuthenticationResponse("accessToken", "refreshToken");

        when(authenticationService.signin(signInRequest)).thenReturn(mockResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.signin(signInRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(authenticationService, times(1)).signin(signInRequest);
    }

    @Test
    void testRefresh() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        JwtAuthenticationResponse mockResponse = new JwtAuthenticationResponse("newAccessToken", "newRefreshToken");

        when(authenticationService.refreshToken(refreshTokenRequest)).thenReturn(mockResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authenticationController.refresh(refreshTokenRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(authenticationService, times(1)).refreshToken(refreshTokenRequest);
    }

    @Test
    void testForgotPassword() {
        String email = "test@example.com";

        // No return value expected
        doNothing().when(authenticationService).initiatePasswordReset(email);

        ResponseEntity<String> response = authenticationController.forgotPassword(email);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password reset link has been sent to your email", response.getBody());
        verify(authenticationService, times(1)).initiatePasswordReset(email);
    }

    @Test
    void testResetPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("reset-token");
        request.setNewPassword("newPassword123");

        // No return value expected
        doNothing().when(authenticationService).resetPassword(request.getToken(), request.getNewPassword());

        ResponseEntity<String> response = authenticationController.resetPassword(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Password has been reset successfully", response.getBody());
        verify(authenticationService, times(1)).resetPassword(request.getToken(), request.getNewPassword());
    }
}
