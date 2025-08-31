package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieMapperImpl implements MovieMapper {

    private final ModelMapper modelMapper;

    @Override
    public Movie toEntity(MovieDTO dto) {
        return modelMapper.map(dto, Movie.class);
    }

    @Override
    public MovieDTO toDTO(Movie entity) {
        return modelMapper.map(entity, MovieDTO.class);
    }
}
