package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;
import com.CinemaGo.model.entity.enums.CrewRole;
import com.CinemaGo.service.MovieCrewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieCrewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MovieCrewService movieCrewService;

    private MovieCrewResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = MovieCrewResponseDTO.builder()
                .id(1L)
                .movieId(10L)
                .personId(20L)
                .role(CrewRole.DIRECTOR)
                .build();
    }

    @Test
    void addPersonToMovie_ShouldReturnCreatedCrew() throws Exception {
        MovieCrewRequestDTO requestDTO = new MovieCrewRequestDTO(
                10L,
                20L,
                CrewRole.DIRECTOR,   // ✅ Enum, not String
                null                 // characterName is optional
        );
        Mockito.when(movieCrewService.addPersonToMovie(any(MovieCrewRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/movie-crew")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.role").value("Director"));
    }

    @Test
    void getById_ShouldReturnCrew() throws Exception {
        Mockito.when(movieCrewService.getById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/movie-crew/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getAll_ShouldReturnListOfCrews() throws Exception {
        Mockito.when(movieCrewService.getAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/movie-crew"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getByMovie_ShouldReturnList() throws Exception {
        Mockito.when(movieCrewService.getByMovie(10L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/movie-crew/movie/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(10L));
    }

    @Test
    void getByPerson_ShouldReturnList() throws Exception {
        Mockito.when(movieCrewService.getByPerson(20L)).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/movie-crew/person/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personId").value(20L));
    }

    @Test
    void update_ShouldReturnUpdatedCrew() throws Exception {
        MovieCrewRequestDTO requestDTO = new MovieCrewRequestDTO(
                10L,
                20L,
                CrewRole.WRITER,   // ✅ Enum, not String
                null                 // characterName is optional
        );
        MovieCrewResponseDTO updatedResponse = MovieCrewResponseDTO.builder()
                .id(1L).movieId(10L).personId(20L).role(CrewRole.WRITER).build();

        Mockito.when(movieCrewService.updateMovieCrew(eq(1L), any(MovieCrewRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/movie-crew/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("Writer"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(movieCrewService).removeFromMovie(1L);

        mockMvc.perform(delete("/api/movie-crew/1"))
                .andExpect(status().isNoContent());
    }
}