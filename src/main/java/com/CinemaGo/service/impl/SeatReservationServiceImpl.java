package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.model.entity.SeatReservation;
import com.CinemaGo.repository.SeatReservationRepository;
import com.CinemaGo.repository.SeatRepository;
import com.CinemaGo.repository.ShowtimeRepository;
import com.CinemaGo.service.SeatReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SeatReservationServiceImpl implements SeatReservationService {

    private final SeatReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    @Override
    public ReservationResponse reserveSeat(ReservationRequest request) {
        if (reservationRepository.existsByShowtimeIdAndSeatId(request.getShowtimeId(), request.getSeatId())) {
            return ReservationResponse.builder()
                    .status("FAILED")
                    .message("Seat already reserved")
                    .build();
        }

        SeatReservation reservation = SeatReservation.builder()
                .showtime(showtimeRepository.findById(request.getShowtimeId()).orElseThrow())
                .seat(seatRepository.findById(request.getSeatId()).orElseThrow())
                .userId(request.getUserId())
                .reservedAt(LocalDateTime.now())
                .status("PENDING") // will confirm after payment
                .build();

        reservationRepository.save(reservation);

        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .status("PENDING")
                .message("Reservation created, waiting for payment")
                .build();
    }
}
