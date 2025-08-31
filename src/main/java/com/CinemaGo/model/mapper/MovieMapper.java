package com.CinemaGo.model.mapper;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.entity.Movie;

public interface MovieMapper {
    Movie toEntity(MovieDTO dto);
    MovieDTO toDTO(Movie entity);
}
