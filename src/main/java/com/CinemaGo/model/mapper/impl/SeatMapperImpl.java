package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.model.entity.Seat;
import com.CinemaGo.model.mapper.SeatMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeatMapperImpl implements SeatMapper {

    private final ModelMapper modelMapper;

    public SeatMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Seat toEntity(SeatRequestDto dto) {
        Seat seat = modelMapper.map(dto, Seat.class);
        return seat;
    }

    @Override
    public SeatResponseDto toDto(Seat entity) {
        return modelMapper.map(entity, SeatResponseDto.class);
    }
}
