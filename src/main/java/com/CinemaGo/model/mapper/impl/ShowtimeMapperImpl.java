package com.CinemaGo.model.mapper.impl;

import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.entity.Showtime;
import com.CinemaGo.model.mapper.ShowtimeMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ShowtimeMapperImpl implements ShowtimeMapper {

    private final ModelMapper modelMapper;

    @Override
    public Showtime toEntity(ShowtimeRequest request) {
        if (request == null) return null;

        Showtime showtime = new Showtime();
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        showtime.setPrice(request.getPrice());
        showtime.setLanguage(request.getLanguage());
        showtime.setFormat(request.getFormat());

        // Custom mapping for movie and hall
        Movie movie = new Movie();
        movie.setId(request.getMovieId());
        showtime.setMovie(movie);

        Hall hall = new Hall();
        hall.setId(request.getHallId());
        showtime.setHall(hall);

        return showtime;
    }

    @Override
    public ShowtimeResponse toResponse(Showtime showtime) {
        ShowtimeResponse response = modelMapper.map(showtime, ShowtimeResponse.class);
        response.setMovieId(showtime.getMovie().getId());
        response.setHallId(showtime.getHall().getId());
        return response;
    }

    @Override
    public void updateEntity(ShowtimeRequest request, Showtime showtime) {
        // Map only simple fields
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getEndTime());
        showtime.setPrice(request.getPrice());
        showtime.setLanguage(request.getLanguage());
        showtime.setFormat(request.getFormat());

        // Manually map IDs to entities
        Movie movie = new Movie();
        movie.setId(request.getMovieId());
        showtime.setMovie(movie);

        Hall hall = new Hall();
        hall.setId(request.getHallId());
        showtime.setHall(hall);
    }
    @Override
    public ShowtimeDTO toDto(Showtime showtime) {
        ShowtimeDTO dto = modelMapper.map(showtime, ShowtimeDTO.class);

        // enrich with movie title & hall name if available
        if (showtime.getMovie() != null) {
            dto.setMovieTitle(showtime.getMovie().getTitle());
        }
        if (showtime.getHall() != null) {
            dto.setHallName(showtime.getHall().getName());
        }

        return dto;
    }

}
