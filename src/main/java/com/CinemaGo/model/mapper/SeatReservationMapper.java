package com.CinemaGo.model.mapper;


import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.model.entity.SeatReservation;

public interface SeatReservationMapper {
    SeatReservation toEntity(ReservationRequest request);
    ReservationResponse toDto(SeatReservation reservation);
}
