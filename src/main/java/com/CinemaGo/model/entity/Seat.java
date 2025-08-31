package com.CinemaGo.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "seats",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"hall_id", "rowLabel", "seatNumber"})})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rowLabel; // A, B, C

    private Integer seatNumber;

    private String seatType = "Standard"; // Standard, VIP, Accessible

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatReservation> reservations;
}
