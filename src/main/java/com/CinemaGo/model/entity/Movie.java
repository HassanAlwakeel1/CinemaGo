package com.CinemaGo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String genre;

    private String posterUrl;

    private Integer durationMinutes;

    private String rating; // PG, PG-13, R

    private LocalDate releaseDate;

    private String language;
    private Boolean subtitlesAvailable = false;
    private String subtitleLanguage;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;
}
