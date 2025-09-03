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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HallServiceImpl implements HallService {
    private static final Logger logger = Logger.getLogger(HallServiceImpl.class.getName());


    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    @Override
    public HallResponseDto createHall(HallRequestDto dto) {
        logger.info("Creating new hall with name: " + dto.getName());
        Hall hall = hallMapper.toEntity(dto);
        Hall savedHall = hallRepository.save(hall);
        logger.info("Hall created successfully with ID: " + savedHall.getId());
        return hallMapper.toDto(savedHall);
    }

    @Override
    public HallResponseDto getHallById(Long id) {
        logger.info("Fetching hall with ID: " + id);
        Hall hall = hallRepository.findById(id).
        orElseThrow(() -> {
            logger.warning("Hall not found with ID: " + id);
            return new ResourceNotFoundException("Hall", "id", id);
        });
        logger.info("Hall retrieved successfully: " + hall.getName());
        return hallMapper.toDto(hall);
    }

    @Override
    public List<HallResponseDto> getAllHalls() {
        logger.info("Fetching all halls");
        List<HallResponseDto> halls = hallRepository.findAll().stream()
                .map(hallMapper::toDto)
                .collect(Collectors.toList());
        logger.info("Total halls found: " + halls.size());
        return halls;
    }

    @Override
    public HallResponseDto updateHall(Long id, HallRequestDto dto) {
        logger.info("Updating hall with ID: " + id);
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warning("Hall not found with ID: " + id);
                    return new ResourceNotFoundException("Hall", "id", id);
                });

        // Map only non-collection fields onto existing entity
        hall.setName(dto.getName());
        hall.setTotalSeats(dto.getTotalSeats());
        hall.setSeatLayout(dto.getSeatLayout());
        hall.setTechnology(dto.getTechnology());
        hall.setAccessibilityFeatures(dto.getAccessibilityFeatures());

        // Important: do NOT overwrite hall.getSeats()

        Hall savedHall = hallRepository.save(hall);
        logger.info("Hall updated successfully with ID: " + savedHall.getId());
        return hallMapper.toDto(savedHall);
    }


    @Override
    public void deleteHall(Long id) {
        logger.info("Request to delete hall with ID: " + id);
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warning("Hall not found with ID: " + id);
                    return new ResourceNotFoundException("Hall", "id", id);
                });
        hallRepository.delete(hall);
        logger.info("Hall deleted successfully with ID: " + id);
    }
}
