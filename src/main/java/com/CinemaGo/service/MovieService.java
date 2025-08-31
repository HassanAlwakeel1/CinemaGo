package com.CinemaGo.service;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;


public interface MovieService {
    MovieDTO createMovie(MovieDTO dto);
    MovieResponseDTO getAllMovies(int pageNumber, int pageSize, String sortBy, String sortDirection);
    MovieDTO getMovie(Long id);
    MovieDTO updateMovie(Long id, MovieDTO dto);
    void deleteMovie(Long id);
}
