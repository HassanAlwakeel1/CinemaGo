package com.CinemaGo.service;

import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;

import java.util.List;

public interface SeatService {
    SeatResponseDto createSeat(SeatRequestDto dto);
    List<SeatResponseDto> getSeatsByHallId(Long hallId);
    SeatResponseDto getSeatById(Long id);
    SeatResponseDto updateSeat(Long id, SeatRequestDto dto);
    void deleteSeat(Long id);
}
