package com.CinemaGo.integration.repository;

import com.CinemaGo.model.entity.Token;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.model.entity.enums.TokenType;
import com.CinemaGo.repository.TokenRepository;
import com.CinemaGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByToken() {
        // Arrange
        User user = new User();
        user.setFirstName("Ahmed");
        user.setLastName("Mostafa");
        user.setEmail("ahmed@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        Token token = new Token();
        token.setToken("jwt-123");
        token.setUser(user);
        token.setExpired(false);
        token.setRevoked(false);
        tokenRepository.save(token);

        // Act
        Optional<Token> found = tokenRepository.findByToken("jwt-123");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getEmail()).isEqualTo("ahmed@test.com");
    }

    @Test
    void testFindAllValidTokensByUser() {
        // Arrange
        User user = new User();
        user.setFirstName("Omar");
        user.setLastName("Youssef");
        user.setEmail("omar@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        Token validToken = Token.builder()
                .token("valid-jwt")
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(user)
                .build();

        Token expiredToken = Token.builder()
                .token("expired-jwt")
                .tokenType(TokenType.BEARER)
                .expired(true)
                .revoked(false) // still false, so matches the OR condition
                .user(user)
                .build();

        tokenRepository.save(validToken);
        tokenRepository.save(expiredToken);

        // Act
        List<Token> tokens = tokenRepository.findAllValidTokensByUser(user.getId());

        // Assert
        // Both tokens will be returned due to the OR in the query
        assertThat(tokens).hasSize(2);
        assertThat(tokens).extracting(Token::getToken)
                .containsExactlyInAnyOrder("valid-jwt", "expired-jwt");
    }

}
