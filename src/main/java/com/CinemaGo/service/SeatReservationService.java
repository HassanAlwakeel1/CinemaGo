package com.CinemaGo.service;


import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;

public interface SeatReservationService {
    ReservationResponse reserveSeat(ReservationRequest request);
}
