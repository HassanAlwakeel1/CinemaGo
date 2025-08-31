package com.CinemaGo.model.mapper;

import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.model.entity.Seat;

public interface SeatMapper {
    Seat toEntity(SeatRequestDto dto);
    SeatResponseDto toDto(Seat entity);
}
