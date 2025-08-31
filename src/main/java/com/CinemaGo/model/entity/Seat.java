package com.CinemaGo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


import com.CinemaGo.model.entity.enums.SeatType;
@Entity
@Table(name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"hall_id", "seatNumber"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    private SeatType seatType = SeatType.STANDARD; // default

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatReservation> reservations=new ArrayList<>();
}
