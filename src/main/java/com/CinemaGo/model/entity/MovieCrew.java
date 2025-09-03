package com.CinemaGo.model.entity;

import com.CinemaGo.model.entity.enums.CrewRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movie_crew")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    @Enumerated(EnumType.STRING)
    private CrewRole role;

    private String characterName; // optional, for actors
}
