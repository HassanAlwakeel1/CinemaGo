package com.CinemaGo.unit.controller;


import com.CinemaGo.controller.ShowtimeController;
import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.service.ShowtimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowtimeControllerTest {

    @Mock
    private ShowtimeService showtimeService;

    @InjectMocks
    private ShowtimeController showtimeController;

    private ShowtimeRequest showtimeRequest;
    private ShowtimeResponse showtimeResponse;

    @BeforeEach
    void setUp() {
        showtimeRequest = new ShowtimeRequest();
        showtimeRequest.setMovieId(1L);
        showtimeRequest.setHallId(2L);
        showtimeRequest.setStartTime(LocalDateTime.now());
        showtimeRequest.setEndTime(LocalDateTime.now().plusHours(2));
        showtimeRequest.setPrice(50.0);
        showtimeRequest.setLanguage("EN");
        showtimeRequest.setFormat("IMAX");

        showtimeResponse = new ShowtimeResponse();
        showtimeResponse.setId(100L);
        showtimeResponse.setMovieId(1L);
        showtimeResponse.setHallId(2L);
        showtimeResponse.setStartTime(showtimeRequest.getStartTime());
        showtimeResponse.setEndTime(showtimeRequest.getEndTime());
        showtimeResponse.setPrice(50.0);
        showtimeResponse.setLanguage("EN");
        showtimeResponse.setFormat("IMAX");
    }

    @Test
    void createShowtime_ShouldReturnCreated() {
        when(showtimeService.createShowtime(showtimeRequest)).thenReturn(showtimeResponse);

        ResponseEntity<ShowtimeResponse> response = showtimeController.createShowtime(showtimeRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(100L, response.getBody().getId());
        verify(showtimeService, times(1)).createShowtime(showtimeRequest);
    }

    @Test
    void getShowtime_ShouldReturnShowtime() {
        when(showtimeService.getShowtime(100L)).thenReturn(showtimeResponse);

        ResponseEntity<ShowtimeResponse> response = showtimeController.getShowtime(100L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100L, response.getBody().getId());
        verify(showtimeService, times(1)).getShowtime(100L);
    }

    @Test
    void updateShowtime_ShouldReturnUpdatedShowtime() {
        when(showtimeService.updateShowtime(100L, showtimeRequest)).thenReturn(showtimeResponse);

        ResponseEntity<ShowtimeResponse> response = showtimeController.updateShowtime(100L, showtimeRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("IMAX", response.getBody().getFormat());
        verify(showtimeService, times(1)).updateShowtime(100L, showtimeRequest);
    }

    @Test
    void deleteShowtime_ShouldReturnNoContent() {
        doNothing().when(showtimeService).deleteShowtime(100L);

        ResponseEntity<Void> response = showtimeController.deleteShowtime(100L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(showtimeService, times(1)).deleteShowtime(100L);
    }

    @Test
    void getShowtimesByMovie_ShouldReturnList() {
        when(showtimeService.getShowtimesByMovie(1L)).thenReturn(List.of(showtimeResponse));

        ResponseEntity<List<ShowtimeResponse>> response = showtimeController.getShowtimesByMovie(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getMovieId());
        verify(showtimeService, times(1)).getShowtimesByMovie(1L);
    }

    @Test
    void filterShowtimes_ShouldReturnFilteredList() {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(200L);
        dto.setMovieId(1L);
        dto.setHallId(2L);
        dto.setLanguage("EN");
        dto.setFormat("IMAX");

        when(showtimeService.filterShowtimes(1L, 2L, "EN", "IMAX",
                showtimeRequest.getStartTime(), showtimeRequest.getEndTime()))
                .thenReturn(Collections.singletonList(dto));

        List<ShowtimeDTO> result = showtimeController.filterShowtimes(
                1L, 2L, "EN", "IMAX", showtimeRequest.getStartTime(), showtimeRequest.getEndTime());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("EN", result.get(0).getLanguage());
        verify(showtimeService, times(1)).filterShowtimes(1L, 2L, "EN", "IMAX",
                showtimeRequest.getStartTime(), showtimeRequest.getEndTime());
    }
}