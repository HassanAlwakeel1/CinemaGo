package com.CinemaGo.model.mapper;

import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.model.entity.Hall;

public interface HallMapper {
    Hall toEntity(HallRequestDto dto);
    HallResponseDto toDto(Hall entity);
}
