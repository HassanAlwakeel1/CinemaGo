package com.CinemaGo.controller;

import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.service.SeatReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/seatReservations")
@RequiredArgsConstructor
@Tag(name = "Seat Reservation")
@CrossOrigin
public class SeatReservationController {
    private final SeatReservationService seatReservationService;
    private static final Logger logger = Logger.getLogger(SeatReservationController.class.getName());


    // Get a reservation by ID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationAdminResponse> getReservationForAdmin(@PathVariable Long id) {
        try {
            ReservationAdminResponse response = seatReservationService.getReservationByIdForAdmin(id);
            logger.info("Fetched reservation for admin with ID: " + id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch reservation for admin with ID: " + id, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ReservationAdminResponse>> getReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long showtimeId,
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) String status
    ) {
        try {
            List<ReservationAdminResponse> reservations = seatReservationService.getReservationsByFilters(
                    userId, showtimeId, movieId, status
            );
            logger.info("Fetched " + reservations.size() + " reservations with filters: " +
                    "userId=" + userId + ", showtimeId=" + showtimeId +
                    ", movieId=" + movieId + ", status=" + status);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch reservations with filters", e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            ReservationResponse response = seatReservationService.updateReservationStatus(id, status);
            logger.info("Updated reservation ID " + id + " status to " + status);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update reservation ID " + id + " status to " + status, e);
            throw e;
        }
    }

    // Delete a reservation
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        try {
            seatReservationService.deleteReservation(id);
            logger.info("Deleted reservation with ID: " + id);
            return ResponseEntity.ok("Reservation deleted successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete reservation with ID: " + id, e);
            throw e;
        }
    }

    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getReservationsByUser(@PathVariable Long userId) {
        try {
            List<ReservationResponse> reservations = seatReservationService.getReservationsByUser(userId);
            logger.info("Fetched " + reservations.size() + " reservations for user ID: " + userId);
            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch reservations for user ID: " + userId, e);
            throw e;
        }
    }
}