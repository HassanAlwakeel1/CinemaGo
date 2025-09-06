package com.CinemaGo.repository;

import com.CinemaGo.model.entity.Seat;
import com.CinemaGo.model.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByHall(Hall hall);
    boolean existsByHallAndSeatNumber(Hall hall, Integer seatNumber);
    int countByHall(Hall hall);

}
