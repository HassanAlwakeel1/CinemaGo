package com.CinemaGo.service;

import com.CinemaGo.model.entity.Token;
import com.CinemaGo.repository.TokenRepository;
import com.CinemaGo.service.impl.LogoutHandlerImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutHandlerImplTest {

    private TokenRepository tokenRepository;
    private LogoutHandlerImpl logoutHandler;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(TokenRepository.class);
        logoutHandler = new LogoutHandlerImpl(tokenRepository);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void logout_NoAuthHeader_ShouldNotCallRepository() {
        when(request.getHeader("Authorization")).thenReturn(null);

        logoutHandler.logout(request, response, null);

        verify(tokenRepository, never()).findByToken(any());
    }

    @Test
    void logout_InvalidAuthHeader_ShouldNotCallRepository() {
        when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        logoutHandler.logout(request, response, null);

        verify(tokenRepository, never()).findByToken(any());
    }

    @Test
    void logout_ValidToken_TokenExists_ShouldExpireAndRevoke() {
        String token = "valid.jwt.token";
        Token storedToken = new Token();
        storedToken.setToken(token);
        storedToken.setExpired(false);
        storedToken.setRevoked(false);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(storedToken));

        logoutHandler.logout(request, response, null);

        // Capture the saved token
        ArgumentCaptor<Token> captor = ArgumentCaptor.forClass(Token.class);
        verify(tokenRepository).save(captor.capture());

        Token savedToken = captor.getValue();
        assertTrue(savedToken.isExpired());
        assertTrue(savedToken.isRevoked());
    }

    @Test
    void logout_ValidToken_TokenNotFound_ShouldNotSave() {
        String token = "valid.jwt.token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.empty());

        logoutHandler.logout(request, response, null);

        verify(tokenRepository, never()).save(any());
    }
}
