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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDto createSeat(SeatRequestDto dto) {
        Hall hall = hallRepository.findById(dto.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", dto.getHallId()));

        boolean exists = seatRepository.existsByHallAndSeatNumber(hall, dto.getSeatNumber());

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Seat number " + dto.getSeatNumber() + " already exists in this hall"
            );
        }

        Seat seat = seatMapper.toEntity(dto);
        seat.setId(null); // crucial
        seat.setHall(hall);

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDto(savedSeat);
    }

    @Override
    public List<SeatResponseDto> getSeatsByHallId(Long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall", "id", hallId));

        List<Seat> seats = seatRepository.findByHall(hall);
        return seats.stream().map(seatMapper::toDto).collect(Collectors.toList());
    }
    @Override
    public SeatResponseDto getSeatById(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", id));
        return seatMapper.toDto(seat);
    }

    @Override
    public SeatResponseDto updateSeat(Long id, SeatRequestDto dto) {
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
        return seatMapper.toDto(savedSeat);
    }

    @Override
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", id));
        seatRepository.delete(seat);
    }
}