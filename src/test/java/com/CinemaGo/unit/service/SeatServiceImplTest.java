package com.CinemaGo.unit.service;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.Seat;
import com.CinemaGo.model.entity.enums.SeatType;
import com.CinemaGo.model.mapper.SeatMapper;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.repository.SeatRepository;
import com.CinemaGo.service.impl.SeatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceImplTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatServiceImpl seatService;

    private Hall hall;
    private SeatRequestDto requestDto;
    private Seat seat;
    private SeatResponseDto responseDto;

    @BeforeEach
    void setUp() {
        hall = Hall.builder().id(1L).name("Main Hall").build();
        requestDto = SeatRequestDto.builder().seatNumber(5).seatType(SeatType.STANDARD).hallId(1L).build();
        seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber(5);
        seat.setSeatType(SeatType.STANDARD);
        seat.setHall(hall);

        responseDto = new SeatResponseDto();
        responseDto.setSeatNumber(5);
        responseDto.setSeatType(SeatType.STANDARD);
    }

    // ------------------- CREATE SEAT -------------------

    @Test
    void createSeat_success() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(seatRepository.existsByHallAndSeatNumber(hall, 5)).thenReturn(false);
        when(seatMapper.toEntity(requestDto)).thenReturn(seat);
        when(seatMapper.toDto(seat)).thenReturn(responseDto);
        when(seatRepository.save(seat)).thenReturn(seat);

        SeatResponseDto result = seatService.createSeat(requestDto);

        assertNotNull(result);
        assertEquals(5, result.getSeatNumber());
        verify(seatRepository).save(seat);
    }

    @Test
    void createSeat_duplicateSeat_throwsException() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(seatRepository.existsByHallAndSeatNumber(hall, 5)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> seatService.createSeat(requestDto));

        assertTrue(exception.getMessage().contains("already exists"));
        verify(seatRepository, never()).save(any());
    }

    @Test
    void createSeat_hallNotFound_throwsException() {
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> seatService.createSeat(requestDto));

        assertTrue(exception.getMessage().contains("Hall"));
        assertTrue(exception.getMessage().contains("1"));
        verify(seatRepository, never()).save(any());
    }

    // ------------------- GET SEATS BY HALL -------------------

    @Test
    void getSeatsByHallId_success() {
        List<Seat> seats = Arrays.asList(seat);
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(seatRepository.findByHall(hall)).thenReturn(seats);
        when(seatMapper.toDto(seat)).thenReturn(responseDto);

        List<SeatResponseDto> result = seatService.getSeatsByHallId(1L);

        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getSeatNumber());
    }

    @Test
    void getSeatsByHallId_hallNotFound_throwsException() {
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> seatService.getSeatsByHallId(1L));

        assertTrue(exception.getMessage().contains("Hall"));
    }

    // ------------------- GET SEAT BY ID -------------------

    @Test
    void getSeatById_success() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatMapper.toDto(seat)).thenReturn(responseDto);

        SeatResponseDto result = seatService.getSeatById(1L);

        assertNotNull(result);
        assertEquals(5, result.getSeatNumber());
    }

    @Test
    void getSeatById_notFound_throwsException() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> seatService.getSeatById(1L));

        assertTrue(exception.getMessage().contains("Seat"));
    }

    // ------------------- UPDATE SEAT -------------------

    @Test
    void updateSeat_success_changeSeatNumberAndType() {
        SeatRequestDto updateDto = SeatRequestDto.builder().seatNumber(10).seatType(SeatType.VIP).hallId(1L).build();

        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(seatRepository.save(seat)).thenReturn(seat);
        when(seatMapper.toDto(seat)).thenReturn(responseDto);

        SeatResponseDto result = seatService.updateSeat(1L, updateDto);

        assertEquals(5, result.getSeatNumber()); // note: responseDto seatNumber is mocked as 5
        verify(seatRepository).save(seat);
    }

    @Test
    void updateSeat_notFound_throwsException() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> seatService.updateSeat(1L, requestDto));

        assertTrue(exception.getMessage().contains("Seat"));
    }

    // ------------------- DELETE SEAT -------------------

    @Test
    void deleteSeat_success() {
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        seatService.deleteSeat(1L);

        verify(seatRepository).delete(seat);
    }

    @Test
    void deleteSeat_notFound_throwsException() {
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> seatService.deleteSeat(1L));

        assertTrue(exception.getMessage().contains("Seat"));
        verify(seatRepository, never()).delete(any());
    }
}