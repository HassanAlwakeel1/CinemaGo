package com.CinemaGo.service;

import com.CinemaGo.model.dto.ShowtimeDTO;

import java.time.LocalDate;
import java.util.List;

public interface filterShowtimesService {
    List<ShowtimeDTO> filterShowtimes(Long movieId, Long hallId, LocalDate date);

}
