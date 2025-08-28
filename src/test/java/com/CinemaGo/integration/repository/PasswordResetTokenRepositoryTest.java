package com.CinemaGo.integration.repository;

import com.CinemaGo.model.entity.PasswordResetToken;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.PasswordResetTokenRepository;
import com.CinemaGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByToken() {
        // Arrange
        User user = new User();
        user.setFirstName("Sara");
        user.setLastName("Ibrahim");
        user.setEmail("sara@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken("reset-123");
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        resetTokenRepository.save(resetToken);

        // Act
        Optional<PasswordResetToken> found = resetTokenRepository.findByToken("reset-123");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getEmail()).isEqualTo("sara@test.com");
    }
}
