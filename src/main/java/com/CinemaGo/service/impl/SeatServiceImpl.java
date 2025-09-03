package com.CinemaGo.service.impl;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.Seat;
import com.CinemaGo.model.mapper.SeatMapper;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.repository.SeatRepository;
import com.CinemaGo.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final Logger logger = LoggerFactory.getLogger(SeatServiceImpl.class);

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDto createSeat(SeatRequestDto dto) {
        logger.info("Creating seat with DTO: {}", dto);

        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", dto.getHallId()));

        boolean exists = seatRepository.existsByHallAndSeatNumber(hall, dto.getSeatNumber());

        if (exists) {
            String errorMessage = "Seat number " + dto.getSeatNumber() + " already exists in this hall";
            logger.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        Seat seat = seatMapper.toEntity(dto);
        seat.setId(null); // crucial
        seat.setHall(hall);

        Seat savedSeat = seatRepository.save(seat);
        logger.info("Created seat with ID: {}", savedSeat.getId());
        return seatMapper.toDto(savedSeat);
    }

    @Override
    @Cacheable(value = "SEATS_BY_HALL", key = "#hallId")
    public List<SeatResponseDto> getSeatsByHallId(Long hallId) {
        logger.info("Fetching seats for hall ID: {}", hallId);

        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", hallId));

        List<Seat> seats = seatRepository.findByHall(hall);
        logger.info("Fetched {} seats for hall ID: {}", seats.size(), hallId);
        return seats.stream().map(seatMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "SEAT_CACHE", key = "#id")
    public SeatResponseDto getSeatById(Long id) {
        logger.info("Fetching seat with ID: {}", id);

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", id));
        logger.info("Fetched seat with ID: {}", seat.getId());
        return seatMapper.toDto(seat);
    }

    @Override
    @CachePut(value = "SEAT_CACHE", key = "#id")
    @CacheEvict(value = "SEATS_BY_HALL", key = "#dto.hallId")
    public SeatResponseDto updateSeat(Long id, SeatRequestDto dto) {
        logger.info("Updating seat with ID: {} and DTO: {}", id, dto);

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", id));

        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatType(dto.getSeatType());

        if (dto.getHallId() != null && !dto.getHallId().equals(seat.getHall().getId())) {
            Hall hall = hallRepository.findById(dto.getHallId())
                    .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", dto.getHallId()));
            seat.setHall(hall);
        }

        Seat savedSeat = seatRepository.save(seat);
        logger.info("Updated seat with ID: {}", savedSeat.getId());
        return seatMapper.toDto(savedSeat);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "HALL_CACHE", key = "#id"),
            @CacheEvict(value = "HALL_LIST_CACHE", allEntries = true)})
    public void deleteSeat(Long id) {
        logger.info("Deleting seat with ID: {}", id);

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", id));
        seatRepository.delete(seat);
        logger.info("Deleted seat with ID: {}", id);
    }
}