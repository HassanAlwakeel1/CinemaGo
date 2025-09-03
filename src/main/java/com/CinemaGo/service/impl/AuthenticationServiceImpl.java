package com.CinemaGo.service.impl;
import com.CinemaGo.model.dto.JwtAuthenticationResponse;
import com.CinemaGo.model.dto.RefreshTokenRequest;
import com.CinemaGo.model.dto.SignInRequest;
import com.CinemaGo.model.dto.SignUpRequest;
import com.CinemaGo.model.entity.PasswordResetToken;
import com.CinemaGo.model.entity.Token;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.model.entity.enums.TokenType;
import com.CinemaGo.repository.PasswordResetTokenRepository;
import com.CinemaGo.repository.TokenRepository;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.AuthenticationService;
import com.CinemaGo.service.JWTService;
import com.CinemaGo.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    private final TokenRepository tokenRepository;

    private final VerificationTokenService tokenService;

    private final JavaMailSender mailSender;

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private static final Logger logger = Logger.getLogger(AuthenticationServiceImpl.class.getName());


    public JwtAuthenticationResponse signup(SignUpRequest signUpRequest){
        logger.info("Signing up new user with email: " + signUpRequest.getEmail());
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(signUpRequest.getRole());
        user.setBio(signUpRequest.getBio());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEnabled(false); // Set user as not enabled

        var savedUser = userRepository.save(user);
        logger.info("User saved successfully: " + savedUser.getId());


        // Create verification token
        VerificationToken token = tokenService.createToken(savedUser);

        // Send verification email
        sendVerificationEmail(savedUser, token.getToken());
        logger.info("Verification email sent to user: " + savedUser.getEmail());

        var jwtToken = jwtService.generateToken(user);
        var jwtRefreshToken = jwtService.generateRefreshToken(new HashMap<>(),user);
        saveUserToken(savedUser, jwtToken);

        return JwtAuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
    private void sendVerificationEmail(User user, String token) {
        String url = "http://localhost:2200/api/v1/auth/verify?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Verify your email");
        email.setText("Click the following link to verify your email: " + url);
        mailSender.send(email);
        logger.info("Verification email sent to " + user.getEmail());
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
        logger.fine("JWT token saved for user: " + user.getId());
    }

    public JwtAuthenticationResponse signin(SignInRequest signinRequest){
        logger.info("Authenticating user: " + signinRequest.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),
                signinRequest.getPassword()));

        var user=userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("Invalid Username or password"));
        logger.info("User authenticated successfully: " + user.getId());
        var jwt = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        revokeAllUserTokens(user);
        saveUserToken(user, jwt);

        return jwtAuthenticationResponse;
    }

    public void revokeAllUserTokens(User user){
        logger.fine("Revoking old tokens for user ID: " + user.getId());
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()){
            logger.fine("No valid tokens found for user ID: " + user.getId());
            return;}
        validUserTokens.forEach(
                token -> {
                    token.setExpired(true);
                    token.setRevoked(true);
                }
        );
        tokenRepository.saveAll(validUserTokens);
        logger.fine("Revoked " + validUserTokens.size() + " tokens for user ID: " + user.getId());
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest)  {
        logger.info("Refreshing token...");
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> {
            logger.warning("Refresh token failed: no user found for email " + userEmail);
            return new IllegalArgumentException("User not found");
        });
        if(jwtService.isTokenValid(refreshTokenRequest.getToken(),user)){
            var jwt = jwtService.generateToken(user);
            logger.info("Refresh token valid, new JWT issued for user ID: " + user.getId());


            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());

            return jwtAuthenticationResponse;
        }
        logger.warning("Invalid refresh token for email: " + userEmail);
        return null;
    }

    public void initiatePasswordReset(String email) {
        logger.info("Initiating password reset for email: " + email);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            logger.warning("Password reset failed: no user found with email " + email);
            throw new UsernameNotFoundException("No user found with email: " + email);
        }
        User user = optionalUser.get();

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Token valid for 1 hour
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = "http://localhost:2200/api/v1/auth/reset-password?token=" + token;
        logger.info("Password reset token created for user ID: " + user.getId());


        sendResetEmail(user.getEmail(), resetUrl);
        logger.info("Password reset email sent to: " + email);

    }

    private void sendResetEmail(String email, String resetUrl) {
        logger.fine("Sending password reset email to: " + email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetUrl);
        mailSender.send(message);
    }

    @Override
    public void resetPassword(String token, String newPassword) {
        logger.info("Resetting password with token: " + token);
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
        if (tokenOptional.isEmpty() || tokenOptional.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.warning("Password reset failed: invalid or expired token");
            throw new IllegalArgumentException("Invalid or expired token");
        }

        User user = tokenOptional.get().getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        logger.info("Password successfully reset for user ID: " + user.getId());


        passwordResetTokenRepository.delete(tokenOptional.get());
        logger.fine("Password reset token invalidated");
    }
}

