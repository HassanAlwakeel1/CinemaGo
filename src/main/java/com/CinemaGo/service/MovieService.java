package com.CinemaGo.service;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;
import com.CinemaGo.model.entity.Movie;

import java.util.List;


public interface MovieService {
    MovieDTO createMovie(MovieDTO dto);
    MovieResponseDTO getAllMovies(int pageNumber, int pageSize, String sortBy, String sortDirection);
    MovieDTO getMovie(Long id);
    MovieDTO updateMovie(Long id, MovieDTO dto);
    void deleteMovie(Long id);

    List<Movie> getNowPlayingMovies();
    List<Movie> getComingSoonMovies();
    void updateMovieAvailability(Long movieId, boolean available);
}
