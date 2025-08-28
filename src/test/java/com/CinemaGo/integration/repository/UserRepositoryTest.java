package com.CinemaGo.integration.repository;

import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.enums.Role;
import com.CinemaGo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User();
        user.setFirstName("Hassan");
        user.setLastName("Elwakeel");
        user.setEmail("hassan@test.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        userRepository.save(user);

        // Act
        Optional<User> found = userRepository.findByEmail("hassan@test.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("Hassan");
    }

    @Test
    void testFindByRole() {
        // Arrange
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@test.com");
        admin.setPassword("adminpass");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);

        // Act
        User found = userRepository.findByRole(Role.ADMIN);

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("admin@test.com");
    }
}
