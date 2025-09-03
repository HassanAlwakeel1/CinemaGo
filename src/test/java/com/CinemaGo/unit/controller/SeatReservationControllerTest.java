package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.SeatReservationController;
import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.service.SeatReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatReservationControllerTest {

    @Mock
    private SeatReservationService seatReservationService;

    @InjectMocks
    private SeatReservationController seatReservationController;

    private ReservationAdminResponse adminResponse;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void setUp() {
        adminResponse = ReservationAdminResponse.builder()
                .reservationId(1L)
                .status("CONFIRMED")
                .showtime(ReservationAdminResponse.ShowtimeInfo.builder()
                        .showtimeId(10L)
                        .movieTitle("Inception")
                        .hallName("Hall 1")
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusHours(2))
                        .price(50.0)
                        .language("EN")
                        .format("IMAX")
                        .build())
                .seat(ReservationAdminResponse.SeatInfo.builder()
                        .seatId(5L)
                        .seatNumber(12)
                        .seatType("VIP")
                        .build())
                .user(ReservationAdminResponse.UserInfo.builder()
                        .userId(100L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build())
                .build();

        reservationResponse = ReservationResponse.builder()
                .reservationId(1L)
                .status("CANCELLED")
                .build();
    }

    @Test
    void getReservationForAdmin_ShouldReturnReservation() {
        when(seatReservationService.getReservationByIdForAdmin(1L)).thenReturn(adminResponse);

        ResponseEntity<ReservationAdminResponse> response = seatReservationController.getReservationForAdmin(1L);

        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getReservationId());
        assertEquals("CONFIRMED", response.getBody().getStatus());
        verify(seatReservationService, times(1)).getReservationByIdForAdmin(1L);
    }

    @Test
    void getReservations_ShouldReturnFilteredList() {
        when(seatReservationService.getReservationsByFilters(100L, 10L, 20L, "CONFIRMED"))
                .thenReturn(List.of(adminResponse));

        ResponseEntity<List<ReservationAdminResponse>> response =
                seatReservationController.getReservations(100L, 10L, 20L, "CONFIRMED");

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(seatReservationService, times(1))
                .getReservationsByFilters(100L, 10L, 20L, "CONFIRMED");
    }

    @Test
    void updateReservationStatus_ShouldReturnUpdatedReservation() {
        when(seatReservationService.updateReservationStatus(1L, "CANCELLED"))
                .thenReturn(reservationResponse);

        ResponseEntity<ReservationResponse> response =
                seatReservationController.updateReservationStatus(1L, "CANCELLED");

        assertNotNull(response.getBody());
        assertEquals("CANCELLED", response.getBody().getStatus());
        verify(seatReservationService, times(1)).updateReservationStatus(1L, "CANCELLED");
    }

    @Test
    void deleteReservation_ShouldReturnSuccessMessage() {
        doNothing().when(seatReservationService).deleteReservation(1L);

        ResponseEntity<String> response = seatReservationController.deleteReservation(1L);

        assertEquals("Reservation deleted successfully", response.getBody());
        verify(seatReservationService, times(1)).deleteReservation(1L);
    }

    @Test
    void getReservationsByUser_ShouldReturnUserReservations() {
        when(seatReservationService.getReservationsByUser(100L))
                .thenReturn(Collections.singletonList(reservationResponse));

        ResponseEntity<List<ReservationResponse>> response =
                seatReservationController.getReservationsByUser(100L);

        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("CANCELLED", response.getBody().get(0).getStatus());
        verify(seatReservationService, times(1)).getReservationsByUser(100L);
    }
}
