package com.CinemaGo.service;


import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;

public interface VerificationTokenService {
    VerificationToken createToken(User user);
    VerificationToken getToken(String token);
    void deleteToken(VerificationToken token);
}
