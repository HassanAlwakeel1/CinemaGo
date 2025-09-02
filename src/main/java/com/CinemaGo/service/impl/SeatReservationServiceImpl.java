package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.model.entity.SeatReservation;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.mapper.SeatReservationMapper;
import com.CinemaGo.repository.SeatReservationRepository;
import com.CinemaGo.repository.SeatRepository;
import com.CinemaGo.repository.ShowtimeRepository;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.SeatReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatReservationServiceImpl implements SeatReservationService {

    private final SeatReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final SeatReservationMapper mapper;
    private final UserRepository userRepository;


    @Override
    public ReservationResponse reserveSeat(ReservationRequest request) {
        if (reservationRepository.existsByShowtimeIdAndSeatId(request.getShowtimeId(), request.getSeatId())) {
            return ReservationResponse.builder()
                    .status("FAILED")
                    .message("Seat already reserved")
                    .build();
        }

        User user = userRepository.findById(request.getUserId()).orElse(null);

        SeatReservation reservation = SeatReservation.builder()
                .showtime(showtimeRepository.findById(request.getShowtimeId()).orElseThrow())
                .seat(seatRepository.findById(request.getSeatId()).orElseThrow())
                .user(user)
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

    @Override
    public ReservationAdminResponse getReservationByIdForAdmin(Long id) {
        SeatReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        return mapper.toAdminDto(reservation);
    }

    @Override
    public List<ReservationAdminResponse> getAllReservationsForAdmin() {
        return reservationRepository.findAll().stream()
                .map(mapper::toAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationResponse updateReservationStatus(Long id, String status) {
        SeatReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservation.setStatus(status);
        return mapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    public void deleteReservation(Long id) {
        SeatReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservationRepository.delete(reservation);
    }
}
