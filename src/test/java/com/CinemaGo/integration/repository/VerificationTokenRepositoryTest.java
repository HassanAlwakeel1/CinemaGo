package com.CinemaGo.integration.repository;

import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.VerificationToken;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class VerificationTokenRepositoryTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private TestEntityManager entityManager; // Useful for persisting related entities

    @Test
    void testFindByToken() {
        User user = new User();
        user.setFirstName("Omar");
        user.setLastName("Youssef");
        user.setEmail("omar@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        entityManager.persist(user);

        VerificationToken token = new VerificationToken();
        token.setToken("sample-token");
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusDays(1));
        entityManager.persist(token);

        VerificationToken fetchedToken = verificationTokenRepository.findByToken("sample-token");

        assertThat(fetchedToken).isNotNull();
        assertThat(fetchedToken.getToken()).isEqualTo("sample-token");
        assertThat(fetchedToken.getUser().getId()).isEqualTo(user.getId());
    }
}

