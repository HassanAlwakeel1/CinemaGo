package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.entity.Showtime;
import com.CinemaGo.model.mapper.ShowtimeMapper;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.repository.ShowtimeRepository;
import com.CinemaGo.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ShowtimeMapper showtimeMapper;

    @Override
    public ShowtimeResponse createShowtime(ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));

        Showtime showtime = showtimeMapper.toEntity(request);
        showtime.setMovie(movie);
        showtime.setHall(hall);

        Showtime saved = showtimeRepository.save(showtime);
        return showtimeMapper.toResponse(saved);
    }

    @Override
    public ShowtimeResponse getShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        return showtimeMapper.toResponse(showtime);
    }

    @Override
    public List<ShowtimeResponse> getAllShowtimes() {
        return showtimeRepository.findAll().stream()
                .map(showtimeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        if (request.getMovieId() != null) {
            Movie movie = movieRepository.findById(request.getMovieId())
                    .orElseThrow(() -> new RuntimeException("Movie not found"));
            showtime.setMovie(movie);
        }

        if (request.getHallId() != null) {
            Hall hall = hallRepository.findById(request.getHallId())
                    .orElseThrow(() -> new RuntimeException("Hall not found"));
            showtime.setHall(hall);
        }

        showtimeMapper.updateEntity(request, showtime);
        Showtime updated = showtimeRepository.save(showtime);
        return showtimeMapper.toResponse(updated);
    }

    @Override
    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }
}
