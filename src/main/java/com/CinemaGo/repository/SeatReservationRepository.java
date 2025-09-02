package com.CinemaGo.repository;

import com.CinemaGo.model.entity.SeatReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {
    List<SeatReservation> findByShowtimeId(Long showtimeId);
    Optional<SeatReservation> findByShowtimeIdAndSeatId(Long showtimeId, Long seatId);
    boolean existsByShowtimeIdAndSeatId(Long showtimeId, Long seatId);

    List<SeatReservation> findByUserId(Long userId);

}
