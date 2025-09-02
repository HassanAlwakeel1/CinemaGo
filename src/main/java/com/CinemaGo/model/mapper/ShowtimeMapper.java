package com.CinemaGo.model.mapper;


import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.model.entity.Showtime;

public interface ShowtimeMapper {
    Showtime toEntity(ShowtimeRequest request);
    ShowtimeResponse toResponse(Showtime showtime);
    void updateEntity(ShowtimeRequest request, Showtime showtime);
    ShowtimeDTO toDto(Showtime showtime);

}
