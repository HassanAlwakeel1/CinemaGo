package com.CinemaGo.service;


import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;

import java.util.List;

public interface SeatReservationService {
    ReservationResponse reserveSeat(ReservationRequest request);
    List<ReservationAdminResponse> getReservationsByFilters(Long userId, Long showtimeId, Long movieId, String status);
    ReservationAdminResponse getReservationByIdForAdmin(Long id);
    ReservationResponse updateReservationStatus(Long id, String status);
    void deleteReservation(Long id);
    List<ReservationResponse> getReservationsByUser(Long userId);
}
