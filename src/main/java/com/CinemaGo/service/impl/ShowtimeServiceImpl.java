package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.ShowtimeDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ShowtimeMapper showtimeMapper;

    private final Logger logger = LoggerFactory.getLogger(ShowtimeServiceImpl.class);

    @Override
    public ShowtimeResponse createShowtime(ShowtimeRequest request) {
        logger.info("Creating showtime for movie ID: " + request.getMovieId() +
                " in hall ID: " + request.getHallId() +
                " at: " + request.getStartTime());

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Hall hall = hallRepository.findById(request.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found"));

        Showtime showtime = showtimeMapper.toEntity(request);
        showtime.setMovie(movie);
        showtime.setHall(hall);

        Showtime saved = showtimeRepository.save(showtime);
        logger.info("Created showtime for movie ID: " + request.getMovieId() +
                " in hall ID: " + request.getHallId() +
                " at: " + request.getStartTime());
        return showtimeMapper.toResponse(saved);
    }

    @Override
    public ShowtimeResponse getShowtime(Long id) {
        logger.info("Getting showtime with ID: " + id);
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        logger.info("Got showtime with ID: " + id);
        return showtimeMapper.toResponse(showtime);
    }

    @Override
    public List<ShowtimeResponse> getAllShowtimes() {
        logger.info("Getting all showtimes");
        List<Showtime> showtimes = showtimeRepository.findAll();
        logger.info("Got all showtimes");
        return showtimes.stream()
                .map(showtimeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request) {
        logger.info("Updating showtime with ID: " + id);
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

    @Override
    public List<ShowtimeResponse> getShowtimesByMovie(Long movieId) {
        List<Showtime> showtimes = showtimeRepository.findByMovieId(movieId);
        return showtimes.stream()
                .map(showtimeMapper::toResponse)
                .toList();
    }
    @Override
    public List<ShowtimeDTO> filterShowtimes(Long movieId,
                                             Long hallId,
                                             String language,
                                             String format,
                                             LocalDateTime startAfter,
                                             LocalDateTime endBefore) {

        List<Showtime> showtimes = showtimeRepository.findAll();

        return showtimes.stream()
                .filter(s -> movieId == null || s.getMovie().getId().equals(movieId))
                .filter(s -> hallId == null || s.getHall().getId().equals(hallId))
                .filter(s -> language == null || s.getLanguage().equalsIgnoreCase(language))
                .filter(s -> format == null || s.getFormat().equalsIgnoreCase(format))
                .filter(s -> startAfter == null || s.getStartTime().isAfter(startAfter))
                .filter(s -> endBefore == null || s.getEndTime().isBefore(endBefore))
                .map(showtimeMapper::toDto)
                .collect(Collectors.toList());
    }
}