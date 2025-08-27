package com.CinemaGo.service;

import com.CinemaGo.model.dto.JwtAuthenticationResponse;
import com.CinemaGo.model.dto.RefreshTokenRequest;
import com.CinemaGo.model.dto.SignInRequest;
import com.CinemaGo.model.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest signUpRequest);

    JwtAuthenticationResponse signin(SignInRequest signinRequest);

    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

    void initiatePasswordReset(String email);

    void resetPassword(String token, String newPassword);
}
