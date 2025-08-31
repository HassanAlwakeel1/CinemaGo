package com.CinemaGo.model.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer totalSeats;

    @Column(columnDefinition = "TEXT")
    private String seatLayout; // can store JSON as String

    private String technology; // IMAX, Dolby Atmos

    @Column(columnDefinition = "TEXT")
    private String accessibilityFeatures;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Showtime> showtimes;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}
