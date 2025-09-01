package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.model.entity.SeatReservation;
import com.CinemaGo.model.mapper.SeatReservationMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeatReservationMapperImpl implements SeatReservationMapper {

    private final ModelMapper modelMapper;

    public SeatReservationMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SeatReservation toEntity(ReservationRequest request) {
        return modelMapper.map(request, SeatReservation.class);
    }

    @Override
    public ReservationResponse toDto(SeatReservation reservation) {
        return modelMapper.map(reservation, ReservationResponse.class);
    }
}
