package com.CinemaGo.service.impl;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.mapper.HallMapper;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    @Override
    public HallResponseDto createHall(HallRequestDto dto) {
        Hall hall = hallMapper.toEntity(dto);
        Hall savedHall = hallRepository.save(hall);
        return hallMapper.toDto(savedHall);
    }

    @Override
    public HallResponseDto getHallById(Long id) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", id));
        return hallMapper.toDto(hall);
    }

    @Override
    public List<HallResponseDto> getAllHalls() {
        return hallRepository.findAll().stream()
                .map(hallMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public HallResponseDto updateHall(Long id, HallRequestDto dto) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", id));

        // Map only non-collection fields onto existing entity
        hall.setName(dto.getName());
        hall.setTotalSeats(dto.getTotalSeats());
        hall.setSeatLayout(dto.getSeatLayout());
        hall.setTechnology(dto.getTechnology());
        hall.setAccessibilityFeatures(dto.getAccessibilityFeatures());

        // Important: do NOT overwrite hall.getSeats()

        Hall savedHall = hallRepository.save(hall);
        return hallMapper.toDto(savedHall);
    }


    @Override
    public void deleteHall(Long id) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", id));
        hallRepository.delete(hall);
    }
}
