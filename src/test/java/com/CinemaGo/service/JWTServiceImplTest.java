package com.CinemaGo.service;

import com.CinemaGo.service.impl.JWTServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class JWTServiceImplTest {

    private JWTServiceImpl jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JWTServiceImpl();
        // Simple concrete UserDetails for testing
        userDetails = new UserDetails() {
            @Override
            public String getUsername() { return "testuser"; }
            @Override public String getPassword() { return "password"; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isEnabled() { return true; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() { return null; }
        };
    }

    @Test
    void generateToken_ShouldReturnNonNullToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void generateRefreshToken_ShouldReturnNonNullToken() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        String token = jwtService.generateRefreshToken(claims, userDetails);
        assertNotNull(token);
    }

    @Test
    void extractUserName_ShouldReturnCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUserName(token);
        assertEquals("testuser", username);
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForWrongUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails otherUser = new UserDetails() {
            @Override public String getUsername() { return "wronguser"; }
            @Override public String getPassword() { return "password"; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isEnabled() { return true; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() { return null; }
        };
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }
}
