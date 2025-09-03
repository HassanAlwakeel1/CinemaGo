package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.HallController;
import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.service.HallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HallControllerTest {

    @Mock
    private HallService hallService;

    @InjectMocks
    private HallController hallController;

    private HallRequestDto hallRequestDto;
    private HallResponseDto hallResponseDto;
    private Hall hall;
    private final Long HALL_ID = 1L;

    @BeforeEach
    void setUp() {
        // Initialize test data
        hallRequestDto = HallRequestDto.builder()
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("A1,A2,A3")
                .technology("Dolby Atmos")
                .accessibilityFeatures("Wheelchair accessible")
                .build();

        hallResponseDto = HallResponseDto.builder()
                .id(HALL_ID)
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("A1,A2,A3")
                .technology("Dolby Atmos")
                .accessibilityFeatures("Wheelchair accessible")
                .build();

        hall = Hall.builder()
                .id(HALL_ID)
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("A1,A2,A3")
                .technology("Dolby Atmos")
                .accessibilityFeatures("Wheelchair accessible")
                .build();
    }

    @Test
    void createHall_ShouldReturnCreatedHall() {
        // Arrange
        when(hallService.createHall(any(HallRequestDto.class))).thenReturn(hallResponseDto);

        // Act
        ResponseEntity<HallResponseDto> response = hallController.createHall(hallRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hallResponseDto, response.getBody());
        verify(hallService, times(1)).createHall(any(HallRequestDto.class));
    }



/* <<<<<<<<<<  d45ddc50-c296-4b91-a51a-df6109e603c4  >>>>>>>>>>> */
    @Test
    void getHallById_ExistingId_ShouldReturnHall() {
        // Arrange
        when(hallService.getHallById(HALL_ID)).thenReturn(hallResponseDto);

        // Act
        ResponseEntity<HallResponseDto> response = hallController.getHallById(HALL_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hallResponseDto, response.getBody());
        verify(hallService, times(1)).getHallById(HALL_ID);
    }

    @Test
    void getAllHalls_ShouldReturnListOfHalls() {
        // Arrange
        List<HallResponseDto> halls = Arrays.asList(hallResponseDto);
        when(hallService.getAllHalls()).thenReturn(halls);

        // Act
        ResponseEntity<List<HallResponseDto>> response = hallController.getAllHalls();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(hallResponseDto, response.getBody().get(0));
        verify(hallService, times(1)).getAllHalls();
    }

    @Test
    void updateHall_ValidId_ShouldReturnUpdatedHall() {
        // Arrange
        when(hallService.updateHall(eq(HALL_ID), any(HallRequestDto.class))).thenReturn(hallResponseDto);

        // Act
        ResponseEntity<HallResponseDto> response = hallController.updateHall(HALL_ID, hallRequestDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(hallResponseDto, response.getBody());
        verify(hallService, times(1)).updateHall(eq(HALL_ID), any(HallRequestDto.class));
    }

    @Test
    void deleteHall_ValidId_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(hallService).deleteHall(HALL_ID);

        // Act
        ResponseEntity<Void> response = hallController.deleteHall(HALL_ID);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(hallService, times(1)).deleteHall(HALL_ID);
    }
}