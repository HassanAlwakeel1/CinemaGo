package com.CinemaGo.controller;

import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.service.SeatReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seatReservations")
@RequiredArgsConstructor
public class SeatReservationController {
    private final SeatReservationService seatReservationService;

    // Get a reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationAdminResponse> getReservationForAdmin(@PathVariable Long id) {
        ReservationAdminResponse response = seatReservationService.getReservationByIdForAdmin(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ReservationAdminResponse>> getAllReservationsForAdmin() {
        List<ReservationAdminResponse> reservations = seatReservationService.getAllReservationsForAdmin();
        return ResponseEntity.ok(reservations);
    }

    // Update reservation status (e.g., cancel a reservation)
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateReservationStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        ReservationResponse response = seatReservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok(response);
    }

    // Delete a reservation
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        seatReservationService.deleteReservation(id);
        return ResponseEntity.ok("Reservation deleted successfully");
    }
}
