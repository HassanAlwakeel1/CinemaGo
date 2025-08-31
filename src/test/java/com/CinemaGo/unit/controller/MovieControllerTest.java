package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.MovieController;
import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;
import com.CinemaGo.service.MovieService;
import org.junit.jupiter.api.Test;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMovie() {
        MovieDTO request = new MovieDTO();
        MovieDTO createdMovie = new MovieDTO();

        when(movieService.createMovie(request)).thenReturn(createdMovie);

        ResponseEntity<MovieDTO> response = movieController.createMovie(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createdMovie, response.getBody());
        verify(movieService, times(1)).createMovie(request);
    }

    @Test
    void testGetAllMovies() {
        MovieResponseDTO mockResponse = new MovieResponseDTO();
        mockResponse.setContent(Collections.singletonList(new MovieDTO()));

        when(movieService.getAllMovies(0, 10, "id", "asc")).thenReturn(mockResponse);

        ResponseEntity<MovieResponseDTO> response = movieController.getAllMovies(0, 10, "id", "asc");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockResponse, response.getBody());
        verify(movieService, times(1)).getAllMovies(0, 10, "id", "asc");
    }

    @Test
    void testGetMovie() {
        Long movieId = 1L;
        MovieDTO mockMovie = new MovieDTO();

        when(movieService.getMovie(movieId)).thenReturn(mockMovie);

        ResponseEntity<MovieDTO> response = movieController.getMovie(movieId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockMovie, response.getBody());
        verify(movieService, times(1)).getMovie(movieId);
    }

    @Test
    void testUpdateMovie() {
        Long movieId = 1L;
        MovieDTO request = new MovieDTO();
        MovieDTO updatedMovie = new MovieDTO();

        when(movieService.updateMovie(movieId, request)).thenReturn(updatedMovie);

        ResponseEntity<MovieDTO> response = movieController.updateMovie(movieId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedMovie, response.getBody());
        verify(movieService, times(1)).updateMovie(movieId, request);
    }

    @Test
    void testDeleteMovie() {
        Long movieId = 1L;

        doNothing().when(movieService).deleteMovie(movieId);

        ResponseEntity<Void> response = movieController.deleteMovie(movieId);

        assertEquals(204, response.getStatusCodeValue());
        verify(movieService, times(1)).deleteMovie(movieId);
    }
}