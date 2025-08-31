package com.CinemaGo.service;

import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;

import java.util.List;

public interface HallService {
    HallResponseDto createHall(HallRequestDto hallRequestDto);
    HallResponseDto getHallById(Long id);
    List<HallResponseDto> getAllHalls();
    HallResponseDto updateHall(Long id, HallRequestDto hallRequestDto);
    void deleteHall(Long id);
}