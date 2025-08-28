package com.CinemaGo.unit.service;

import com.CinemaGo.model.dto.JwtAuthenticationResponse;
import com.CinemaGo.model.dto.RefreshTokenRequest;
import com.CinemaGo.model.dto.SignInRequest;
import com.CinemaGo.model.dto.SignUpRequest;
import com.CinemaGo.model.entity.PasswordResetToken;
import com.CinemaGo.model.entity.Token;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.PasswordResetTokenRepository;
import com.CinemaGo.repository.TokenRepository;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.JWTService;
import com.CinemaGo.service.VerificationTokenService;
import com.CinemaGo.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.mockito.ArgumentMatchers;

import java.time.LocalDateTime;
import java.util.*;


@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JWTService jwtService;
    @Mock private TokenRepository tokenRepository;
    @Mock private VerificationTokenService verificationTokenService;
    @Mock private JavaMailSender mailSender;
    @Mock private PasswordResetTokenRepository passwordResetTokenRepository;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    @Test
    void signup_ShouldSaveUserAndSendVerificationEmail() {
        // Arrange
        SignUpRequest request = new SignUpRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john@mail.com");
        request.setPassword("pass123");
        request.setBio("Bio");
        request.setRole(Role.USER);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("john@mail.com");

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken("verif-token");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(verificationTokenService.createToken(any(User.class))).thenReturn(verificationToken);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");
        when(jwtService.generateRefreshToken(ArgumentMatchers.<HashMap<String, Object>>any(), any(User.class)))
                .thenReturn("refresh-token");

        // Act
        JwtAuthenticationResponse response = authenticationService.signup(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());

        verify(userRepository).save(any(User.class));
        verify(verificationTokenService).createToken(savedUser);
        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void signin_ShouldAuthenticateUserAndReturnTokens() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setEmail("john@mail.com");
        request.setPassword("pass123");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("john@mail.com");
        existingUser.setPassword("encodedPass");

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(existingUser));
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).willReturn(null);
        given(jwtService.generateToken(existingUser)).willReturn("jwt-token");
        given(jwtService.generateRefreshToken(ArgumentMatchers.<HashMap<String, Object>>any(), eq(existingUser)))
                .willReturn("refresh-token");

        // Act
        JwtAuthenticationResponse response = authenticationService.signin(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("refresh-token", response.getRefreshToken());

        then(authenticationManager).should().authenticate(any(UsernamePasswordAuthenticationToken.class));
        then(userRepository).should().findByEmail("john@mail.com");
        then(jwtService).should().generateToken(existingUser);
        then(jwtService).should().generateRefreshToken(ArgumentMatchers.<HashMap<String, Object>>any(), eq(existingUser));
    }

    @Test
    void revokeAllUserTokens_ShouldMarkTokensAsRevokedAndExpired() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Token token1 = new Token();
        token1.setExpired(false);
        token1.setRevoked(false);

        Token token2 = new Token();
        token2.setExpired(false);
        token2.setRevoked(false);

        List<Token> validTokens = List.of(token1, token2);

        given(tokenRepository.findAllValidTokensByUser(user.getId())).willReturn(validTokens);

        // Act
        authenticationService.revokeAllUserTokens(user);

        // Assert
        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
        assertTrue(token2.isExpired());
        assertTrue(token2.isRevoked());
        then(tokenRepository).should().saveAll(validTokens);
    }


    @Test
    void revokeAllUserTokens_ShouldDoNothing_WhenNoValidTokensExist() {
        // Arrange
        User user = new User();
        user.setId(1L);

        given(tokenRepository.findAllValidTokensByUser(user.getId()))
                .willReturn(Collections.emptyList());

        // Act
        authenticationService.revokeAllUserTokens(user);

        // Assert
        then(tokenRepository).should().findAllValidTokensByUser(user.getId());
        then(tokenRepository).should(never()).saveAll(anyList());
    }

    @Test
    void refreshToken_ShouldReturnNewJwt_WhenRefreshTokenIsValid() {
        // Arrange
        String refreshToken = "valid-refresh-token";
        String email = "john@mail.com";
        User existingUser = new User();
        existingUser.setEmail(email);

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        given(jwtService.extractUserName(refreshToken)).willReturn(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(existingUser));
        given(jwtService.isTokenValid(refreshToken, existingUser)).willReturn(true);
        given(jwtService.generateToken(existingUser)).willReturn("new-jwt-token");

        // Act
        JwtAuthenticationResponse response = authenticationService.refreshToken(request);

        // Assert
        assertNotNull(response);
        assertEquals("new-jwt-token", response.getToken());
        assertEquals(refreshToken, response.getRefreshToken());

        then(jwtService).should().extractUserName(refreshToken);
        then(jwtService).should().isTokenValid(refreshToken, existingUser);
        then(jwtService).should().generateToken(existingUser);
    }

    @Test
    void refreshToken_ShouldReturnNull_WhenTokenIsInvalid() {
        // Arrange
        String refreshToken = "invalid-refresh-token";
        String email = "john@mail.com";
        User existingUser = new User();
        existingUser.setEmail(email);

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        given(jwtService.extractUserName(refreshToken)).willReturn(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(existingUser));
        given(jwtService.isTokenValid(refreshToken, existingUser)).willReturn(false);

        // Act
        JwtAuthenticationResponse response = authenticationService.refreshToken(request);

        // Assert
        assertNull(response);

        then(jwtService).should().isTokenValid(refreshToken, existingUser);
        then(jwtService).should(never()).generateToken(any(User.class));
    }

    @Test
    void refreshToken_ShouldThrow_WhenUserNotFound() {
        // Arrange
        String refreshToken = "refresh-token";
        String email = "missing@mail.com";
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        given(jwtService.extractUserName(refreshToken)).willReturn(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class,
                () -> authenticationService.refreshToken(request));

        then(userRepository).should().findByEmail(email);
        then(jwtService).should(never()).isTokenValid(anyString(), any(User.class));
    }

    @Test
    void initiatePasswordReset_ShouldSendEmail_WhenUserExists() {
        // Arrange
        String email = "john@mail.com";
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail(email);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(existingUser));

        // Act
        authenticationService.initiatePasswordReset(email);

        // Assert
        then(userRepository).should().findByEmail(email);
        then(passwordResetTokenRepository).should().save(any());
        then(mailSender).should().send(any(SimpleMailMessage.class));
    }


    @Test
    void initiatePasswordReset_ShouldThrow_WhenUserNotFound() {
        // Arrange
        String email = "missing@mail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.initiatePasswordReset(email));

        then(userRepository).should().findByEmail(email);
        then(passwordResetTokenRepository).should(never()).save(any());
        then(mailSender).should(never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void signin_ShouldThrow_WhenUserNotFound() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setEmail("notfound@mail.com");
        request.setPassword("password");

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authenticationService.signin(request));
    }



    @Test
    void signin_ShouldThrow_WhenPasswordIncorrect() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setEmail("user@mail.com");
        request.setPassword("wrongPassword");

        willThrow(new BadCredentialsException("Bad credentials"))
                .given(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authenticationService.signin(request));
    }

    @Test
    void resetPassword_ShouldThrow_WhenTokenExpired() {
        // Arrange
        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setExpiryDate(LocalDateTime.now().minusHours(1));
        expiredToken.setToken("expired-token");
        expiredToken.setUser(new User());

        given(passwordResetTokenRepository.findByToken("expired-token")).willReturn(Optional.of(expiredToken));

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.resetPassword("expired-token", "newPass"));
    }


    @Test
    void resetPassword_ShouldThrow_WhenTokenNotFound() {
        // Arrange
        given(passwordResetTokenRepository.findByToken("missing-token")).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.resetPassword("missing-token", "newPass"));
    }


    @Test
    void resetPassword_ShouldEncodePassword() {
        // Arrange
        User user = new User();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken("valid-token");
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
        token.setUser(user);

        given(passwordResetTokenRepository.findByToken("valid-token")).willReturn(Optional.of(token));

        // Act
        authenticationService.resetPassword("valid-token", "newPass");

        // Assert
        assertNotEquals("newPass", user.getPassword(), "Password should be encoded and not equal to raw input");
    }

}
