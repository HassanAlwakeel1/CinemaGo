package com.CinemaGo.service.impl;

import com.CinemaGo.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class LogoutHandlerImpl implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutHandlerImpl.class);

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logger.info("Logging out user...");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (!StringUtils.hasText(authHeader) ||
                !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer ")) {
            logger.info("No Authorization header or invalid token format found");
            return;
        }

        jwt = authHeader.substring(7);

        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            logger.info("Found token in repository, expiring and revoking");
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }

        logger.info("Logged out user");
    }
}