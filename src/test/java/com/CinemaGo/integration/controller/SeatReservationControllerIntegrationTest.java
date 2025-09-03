package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.service.SeatReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SeatReservationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SeatReservationService seatReservationService;

    private ReservationAdminResponse adminResponse;
    private ReservationResponse reservationResponse;

    @BeforeEach
    void setUp() {
        adminResponse = ReservationAdminResponse.builder()
                .reservationId(1L)
                .status("CONFIRMED")
                .showtime(ReservationAdminResponse.ShowtimeInfo.builder()
                        .showtimeId(100L)
                        .movieTitle("Inception")
                        .hallName("Hall 1")
                        .startTime(LocalDateTime.now())
                        .endTime(LocalDateTime.now().plusHours(2))
                        .price(100.0)
                        .language("EN")
                        .format("IMAX")
                        .build())
                .seat(ReservationAdminResponse.SeatInfo.builder()
                        .seatId(200L)
                        .seatNumber(10)
                        .seatType("VIP")
                        .build())
                .user(ReservationAdminResponse.UserInfo.builder()
                        .userId(300L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build())
                .build();

        reservationResponse = ReservationResponse.builder()
                .reservationId(1L)
                .status("CANCELLED")
                .message("Reservation updated successfully")
                .build();
    }

    @Test
    void getReservationForAdmin_ShouldReturnReservation() throws Exception {
        Mockito.when(seatReservationService.getReservationByIdForAdmin(1L)).thenReturn(adminResponse);

        mockMvc.perform(get("/api/v1/seatReservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1L))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.showtime.movieTitle").value("Inception"));
    }

    @Test
    void getReservations_ShouldReturnList() throws Exception {
        Mockito.when(seatReservationService.getReservationsByFilters(null, null, null, null))
                .thenReturn(List.of(adminResponse));

        mockMvc.perform(get("/api/v1/seatReservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(1L));
    }

    @Test
    void updateReservationStatus_ShouldReturnUpdatedReservation() throws Exception {
        Mockito.when(seatReservationService.updateReservationStatus(eq(1L), eq("CANCELLED")))
                .thenReturn(reservationResponse);

        mockMvc.perform(put("/api/v1/seatReservations/1")
                        .param("status", "CANCELLED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId").value(1L))
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void deleteReservation_ShouldReturnSuccessMessage() throws Exception {
        Mockito.doNothing().when(seatReservationService).deleteReservation(1L);

        mockMvc.perform(delete("/api/v1/seatReservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation deleted successfully"));
    }

    @Test
    void getReservationsByUser_ShouldReturnList() throws Exception {
        Mockito.when(seatReservationService.getReservationsByUser(300L)).thenReturn(List.of(reservationResponse));

        mockMvc.perform(get("/api/v1/seatReservations/user/300"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(1L))
                .andExpect(jsonPath("$[0].status").value("CANCELLED"));
    }
}
