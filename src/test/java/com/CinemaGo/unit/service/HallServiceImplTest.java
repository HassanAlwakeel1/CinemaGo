package com.CinemaGo.unit.service;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.mapper.HallMapper;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.service.impl.HallServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HallServiceImplTest {

    @Mock
    private HallRepository hallRepository;

    @Mock
    private HallMapper hallMapper;

    @InjectMocks
    private HallServiceImpl hallService;

    private Hall hall;
    private HallRequestDto hallRequestDto;
    private HallResponseDto hallResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hallRequestDto = HallRequestDto.builder()
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("Wheelchair")
                .build();

        hall = Hall.builder()
                .id(1L)
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("Wheelchair")
                .build();

        hallResponseDto = HallResponseDto.builder()
                .id(1L)
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("Wheelchair")
                .build();
    }

    @Test
    void testCreateHall() {
        when(hallMapper.toEntity(hallRequestDto)).thenReturn(hall);
        when(hallRepository.save(hall)).thenReturn(hall);
        when(hallMapper.toDto(hall)).thenReturn(hallResponseDto);

        HallResponseDto response = hallService.createHall(hallRequestDto);

        assertNotNull(response);
        assertEquals("Hall 1", response.getName());
        verify(hallRepository, times(1)).save(hall);
    }

    @Test
    void testGetHallById_Success() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(hallMapper.toDto(hall)).thenReturn(hallResponseDto);

        HallResponseDto response = hallService.getHallById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    @Test
    void testGetHallById_NotFound() {
        when(hallRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> hallService.getHallById(1L));
    }

    @Test
    void testGetAllHalls() {
        when(hallRepository.findAll()).thenReturn(Arrays.asList(hall));
        when(hallMapper.toDto(hall)).thenReturn(hallResponseDto);

        List<HallResponseDto> halls = hallService.getAllHalls();

        assertEquals(1, halls.size());
        verify(hallRepository, times(1)).findAll();
    }

    @Test
    void testUpdateHall() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        // manually map fields instead of creating new object
        when(hallRepository.save(hall)).thenReturn(hall);
        when(hallMapper.toDto(hall)).thenReturn(hallResponseDto);

        HallResponseDto updated = hallService.updateHall(1L, hallRequestDto);

        assertEquals("Hall 1", updated.getName());
        verify(hallRepository).save(hall);
    }

    @Test
    void testDeleteHall() {
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));

        hallService.deleteHall(1L);

        verify(hallRepository, times(1)).delete(hall);
    }
}

