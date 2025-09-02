package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.ReservationAdminResponse;
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
    @Override
    public ReservationAdminResponse toAdminDto(SeatReservation reservation) {
        if (reservation == null) return null;

        return ReservationAdminResponse.builder()
                .reservationId(reservation.getId())
                .status(reservation.getStatus())
                .showtime(
                        ReservationAdminResponse.ShowtimeInfo.builder()
                                .showtimeId(reservation.getShowtime().getId())
                                .movieTitle(reservation.getShowtime().getMovie().getTitle())
                                .hallName(reservation.getShowtime().getHall().getName())
                                .startTime(reservation.getShowtime().getStartTime())
                                .endTime(reservation.getShowtime().getEndTime())
                                .price(reservation.getShowtime().getPrice())
                                .language(reservation.getShowtime().getLanguage())
                                .format(reservation.getShowtime().getFormat())
                                .build()
                )
                .seat(
                        ReservationAdminResponse.SeatInfo.builder()
                                .seatId(reservation.getSeat().getId())
                                .seatNumber(reservation.getSeat().getSeatNumber())
                                .seatType(reservation.getSeat().getSeatType().name())
                                .build()
                )
                .user(
                        ReservationAdminResponse.UserInfo.builder()
                                .userId(reservation.getUser() != null ? reservation.getUser().getId() : null)
                                .firstName(reservation.getUser() != null ? reservation.getUser().getFirstName() : null)
                                .lastName(reservation.getUser() != null ? reservation.getUser().getLastName() : null)
                                .email(reservation.getUser() != null ? reservation.getUser().getEmail() : null)
                                .build()
                )
                .build();
    }


}
