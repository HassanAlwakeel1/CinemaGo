package com.CinemaGo.integration.repository;

import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.Seat;
import com.CinemaGo.model.entity.enums.SeatType;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private HallRepository hallRepository;

    private Hall hall;

    @BeforeEach
    void setUp() {
        seatRepository.deleteAll();
        hallRepository.deleteAll();

        hall = hallRepository.save(Hall.builder()
                .name("Test Hall")
                .totalSeats(50)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("Wheelchair")
                .build());
    }

    @Test
    void existsByHallAndSeatNumber_shouldReturnTrueIfExists() {
        Seat seat = Seat.builder()
                .seatNumber(1)
                .seatType(SeatType.STANDARD)
                .hall(hall)
                .build();
        seatRepository.save(seat);

        boolean exists = seatRepository.existsByHallAndSeatNumber(hall, 1);
        assertThat(exists).isTrue();
    }

    @Test
    void findByHall_shouldReturnAllSeatsForHall() {
        Seat seat1 = Seat.builder().seatNumber(1).seatType(SeatType.STANDARD).hall(hall).build();
        Seat seat2 = Seat.builder().seatNumber(2).seatType(SeatType.VIP).hall(hall).build();
        seatRepository.save(seat1);
        seatRepository.save(seat2);

        List<Seat> seats = seatRepository.findByHall(hall);
        assertThat(seats).hasSize(2).extracting("seatNumber").containsExactlyInAnyOrder(1, 2);
    }
}