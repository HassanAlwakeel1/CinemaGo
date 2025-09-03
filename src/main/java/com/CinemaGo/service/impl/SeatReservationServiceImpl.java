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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatReservationServiceImpl implements SeatReservationService {

    private static final Logger logger = LoggerFactory.getLogger(SeatReservationServiceImpl.class);

    private final SeatReservationRepository reservationRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final SeatReservationMapper mapper;
    private final UserRepository userRepository;


    @Override
    public ReservationResponse reserveSeat(ReservationRequest request) {
        logger.info("Reserving seat with request: " + request);
        if (reservationRepository.existsByShowtimeIdAndSeatId(request.getShowtimeId(), request.getSeatId())) {
            logger.info("Seat already reserved");
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
        logger.info("Reservation created with id: " + reservation.getId());

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
        logger.info("Fetched reservation with id: " + id);
        return mapper.toAdminDto(reservation);
    }

    @Override
    public List<ReservationAdminResponse> getReservationsByFilters(Long userId, Long showtimeId, Long movieId, String status) {
        logger.info("Fetching reservations by filters: userId=" + userId + ", showtimeId=" + showtimeId + ", movieId=" + movieId + ", status=" + status);
        List<SeatReservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .filter(r -> userId == null || (r.getUser() != null && r.getUser().getId().equals(userId)))
                .filter(r -> showtimeId == null || (r.getShowtime() != null && r.getShowtime().getId().equals(showtimeId)))
                .filter(r -> movieId == null ||
                        (r.getShowtime() != null && r.getShowtime().getMovie() != null
                                && r.getShowtime().getMovie().getId().equals(movieId)))
                .filter(r -> status == null || (r.getStatus() != null && r.getStatus().equalsIgnoreCase(status)))
                .map(mapper::toAdminDto)
                .toList();
    }

    @Override
    public ReservationResponse updateReservationStatus(Long id, String status) {
        logger.info("Updating reservation status with id: " + id + " to status: " + status);
        SeatReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservation.setStatus(status);
        return mapper.toDto(reservationRepository.save(reservation));
    }

    @Override
    public void deleteReservation(Long id) {
        logger.info("Deleting reservation with id: " + id);
        SeatReservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));
        reservationRepository.delete(reservation);
    }

    @Override
    public List<ReservationResponse> getReservationsByUser(Long userId) {
        logger.info("Fetching reservations by userId: " + userId);
        List<SeatReservation> reservations = reservationRepository.findByUserId(userId);

        return reservations.stream()
                .map(reservation -> ReservationResponse.builder()
                        .reservationId(reservation.getId())
                        .status(reservation.getStatus())
                        .message("Reservation retrieved successfully")
                        .build())
                .toList();
    }
}