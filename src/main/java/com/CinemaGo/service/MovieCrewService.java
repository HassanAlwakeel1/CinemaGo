package com.CinemaGo.service;

import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;

import java.util.List;

public interface MovieCrewService {
    MovieCrewResponseDTO addPersonToMovie(MovieCrewRequestDTO dto);
    MovieCrewResponseDTO getById(Long id);
    List<MovieCrewResponseDTO> getAll();
    List<MovieCrewResponseDTO> getByMovie(Long movieId);
    List<MovieCrewResponseDTO> getByPerson(Long personId);
    MovieCrewResponseDTO updateMovieCrew(Long id, MovieCrewRequestDTO dto);
    void removeFromMovie(Long id);
}