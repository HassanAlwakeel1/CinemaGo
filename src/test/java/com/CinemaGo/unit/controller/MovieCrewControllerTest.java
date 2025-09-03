package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.MovieCrewController;
import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;
import com.CinemaGo.model.entity.enums.CrewRole;
import com.CinemaGo.service.MovieCrewService;
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
class MovieCrewControllerTest {

    @Mock
    private MovieCrewService movieCrewService;

    @InjectMocks
    private MovieCrewController movieCrewController;

    private MovieCrewRequestDTO requestDTO;
    private MovieCrewResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new MovieCrewRequestDTO();
        requestDTO.setMovieId(1L);
        requestDTO.setPersonId(2L);
        requestDTO.setRole(CrewRole.ACTOR);
        requestDTO.setCharacterName("Hero");

        responseDTO = MovieCrewResponseDTO.builder()
                .id(10L)
                .movieId(1L)
                .movieTitle("Inception")
                .personId(2L)
                .personName("Leonardo DiCaprio")
                .role(CrewRole.ACTOR)
                .characterName("Cobb")
                .build();
    }

    @Test
    void addPersonToMovie_ShouldReturnResponse() {
        when(movieCrewService.addPersonToMovie(any(MovieCrewRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<MovieCrewResponseDTO> response = movieCrewController.addPersonToMovie(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(movieCrewService, times(1)).addPersonToMovie(requestDTO);
    }

    @Test
    void getById_ShouldReturnMovieCrew() {
        when(movieCrewService.getById(anyLong())).thenReturn(responseDTO);

        ResponseEntity<MovieCrewResponseDTO> response = movieCrewController.getById(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(movieCrewService, times(1)).getById(10L);
    }

    @Test
    void getAll_ShouldReturnList() {
        when(movieCrewService.getAll()).thenReturn(Collections.singletonList(responseDTO));

        ResponseEntity<List<MovieCrewResponseDTO>> response = movieCrewController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(movieCrewService, times(1)).getAll();
    }

    @Test
    void getByMovie_ShouldReturnList() {
        when(movieCrewService.getByMovie(anyLong())).thenReturn(Collections.singletonList(responseDTO));

        ResponseEntity<List<MovieCrewResponseDTO>> response = movieCrewController.getByMovie(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(movieCrewService, times(1)).getByMovie(1L);
    }

    @Test
    void getByPerson_ShouldReturnList() {
        when(movieCrewService.getByPerson(anyLong())).thenReturn(Collections.singletonList(responseDTO));

        ResponseEntity<List<MovieCrewResponseDTO>> response = movieCrewController.getByPerson(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(movieCrewService, times(1)).getByPerson(2L);
    }

    @Test
    void update_ShouldReturnUpdatedMovieCrew() {
        when(movieCrewService.updateMovieCrew(anyLong(), any(MovieCrewRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<MovieCrewResponseDTO> response = movieCrewController.update(10L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(movieCrewService, times(1)).updateMovieCrew(10L, requestDTO);
    }

    @Test
    void delete_ShouldReturnNoContent() {
        doNothing().when(movieCrewService).removeFromMovie(anyLong());

        ResponseEntity<Void> response = movieCrewController.delete(10L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(movieCrewService, times(1)).removeFromMovie(10L);
    }
}