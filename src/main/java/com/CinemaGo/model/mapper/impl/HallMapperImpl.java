package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.mapper.HallMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class HallMapperImpl implements HallMapper {

    private final ModelMapper modelMapper;

    public HallMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Hall toEntity(HallRequestDto dto) {
        return modelMapper.map(dto, Hall.class);
    }

    @Override
    public HallResponseDto toDto(Hall entity) {
        return modelMapper.map(entity, HallResponseDto.class);
    }
}
