package com.CinemaGo.service.impl;

import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.repository.VerificationTokenRepository;
import com.CinemaGo.service.VerificationTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final Logger logger = LoggerFactory.getLogger(VerificationTokenServiceImpl.class);
    private final VerificationTokenRepository tokenRepository;

    // Constructor injection
    public VerificationTokenServiceImpl(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
        logger.info("VerificationTokenServiceImpl instantiated");
    }

    @Override
    public VerificationToken createToken(User user) {
        logger.debug("Creating verification token for user");
        VerificationToken token = new VerificationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(1)); // Token valid for 1 day
        return tokenRepository.save(token);
    }

    @Override
    public VerificationToken getToken(String token) {
        logger.debug("Fetching verification token for token {}", token);
        return tokenRepository.findByToken(token);
    }

    @Override
    public void deleteToken(VerificationToken token) {
        logger.debug("Deleting verification token {}", token.getToken());
        tokenRepository.delete(token);
    }
}