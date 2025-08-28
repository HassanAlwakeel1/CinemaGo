package com.CinemaGo.unit.service;

import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.repository.VerificationTokenRepository;
import com.CinemaGo.service.impl.VerificationTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VerificationTokenServiceImplTest {

    private VerificationTokenRepository tokenRepository;
    private VerificationTokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        tokenRepository = mock(VerificationTokenRepository.class);
        tokenService = new VerificationTokenServiceImpl(tokenRepository); // inject via constructor
    }

    @Test
    void createToken_ShouldGenerateAndSaveToken() {
        User user = new User();
        VerificationToken savedToken = new VerificationToken();
        when(tokenRepository.save(any(VerificationToken.class))).thenReturn(savedToken);

        VerificationToken result = tokenService.createToken(user);

        ArgumentCaptor<VerificationToken> captor = ArgumentCaptor.forClass(VerificationToken.class);
        verify(tokenRepository).save(captor.capture());

        VerificationToken captured = captor.getValue();
        assertEquals(user, captured.getUser());
        assertNotNull(captured.getToken());
        assertTrue(captured.getExpiryDate().isAfter(LocalDateTime.now()));

        assertEquals(savedToken, result);
    }

    @Test
    void getToken_ShouldReturnTokenFromRepository() {
        String tokenString = "abc123";
        VerificationToken token = new VerificationToken();
        when(tokenRepository.findByToken(tokenString)).thenReturn(token);

        VerificationToken result = tokenService.getToken(tokenString);

        verify(tokenRepository).findByToken(tokenString);
        assertEquals(token, result);
    }

    @Test
    void deleteToken_ShouldCallRepositoryDelete() {
        VerificationToken token = new VerificationToken();

        tokenService.deleteToken(token);

        verify(tokenRepository).delete(token);
    }
}
