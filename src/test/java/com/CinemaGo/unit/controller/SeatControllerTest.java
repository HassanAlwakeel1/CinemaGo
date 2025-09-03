package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.SeatController;
import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.model.entity.enums.SeatType;
import com.CinemaGo.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatControllerTest {

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    private SeatRequestDto requestDto;
    private SeatResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = SeatRequestDto.builder()
                .seatNumber(5)
                .seatType(SeatType.STANDARD)
                .hallId(1L)
                .build();

        responseDto = SeatResponseDto.builder()
                .id(1L)
                .seatNumber(5)
                .seatType(SeatType.STANDARD)
                .hallId(1L)
                .build();
    }

    @Test
    void createSeat_ShouldReturnCreatedSeat() {
        when(seatService.createSeat(any(SeatRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<SeatResponseDto> response = seatController.createSeat(requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(seatService, times(1)).createSeat(requestDto);
    }

    @Test
    void getSeatsByHall_ShouldReturnListOfSeats() {
        when(seatService.getSeatsByHallId(anyLong())).thenReturn(Collections.singletonList(responseDto));

        ResponseEntity<List<SeatResponseDto>> response = seatController.getSeatsByHall(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(seatService, times(1)).getSeatsByHallId(1L);
    }

    @Test
    void getSeatById_ShouldReturnSeat() {
        when(seatService.getSeatById(anyLong())).thenReturn(responseDto);

        ResponseEntity<SeatResponseDto> response = seatController.getSeatById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(seatService, times(1)).getSeatById(1L);
    }

    @Test
    void updateSeat_ShouldReturnUpdatedSeat() {
        when(seatService.updateSeat(anyLong(), any(SeatRequestDto.class))).thenReturn(responseDto);

        ResponseEntity<SeatResponseDto> response = seatController.updateSeat(1L, requestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDto, response.getBody());
        verify(seatService, times(1)).updateSeat(1L, requestDto);
    }

    @Test
    void deleteSeat_ShouldReturnNoContent() {
        doNothing().when(seatService).deleteSeat(anyLong());

        ResponseEntity<Void> response = seatController.deleteSeat(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(seatService, times(1)).deleteSeat(1L);
    }
}
