package com.CinemaGo.repository;

import com.CinemaGo.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :today AND m.available = true")
    List<Movie> findNowPlayingMovies(@Param("today") LocalDate today);

    @Query("SELECT m FROM Movie m " +
            "WHERE m.available = true AND m.releaseDate > :today")
    List<Movie> findComingSoonMovies(LocalDate today);
}
