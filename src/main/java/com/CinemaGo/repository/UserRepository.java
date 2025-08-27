package com.CinemaGo.repository;

import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User findByRole(Role role);

}
