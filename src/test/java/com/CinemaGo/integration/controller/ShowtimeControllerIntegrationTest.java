package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.service.ShowtimeService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ShowtimeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShowtimeService showtimeService;

    private ShowtimeRequest request;
    private ShowtimeResponse response;

    @BeforeEach
    void setUp() {
        request = new ShowtimeRequest();
        request.setMovieId(10L);
        request.setHallId(20L);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(2));
        request.setPrice(150.0);
        request.setLanguage("EN");
        request.setFormat("IMAX");

        response = new ShowtimeResponse();
        response.setId(1L);
        response.setMovieId(10L);
        response.setHallId(20L);
        response.setStartTime(request.getStartTime());
        response.setEndTime(request.getEndTime());
        response.setPrice(request.getPrice());
        response.setLanguage(request.getLanguage());
        response.setFormat(request.getFormat());
    }

    @Test
    void createShowtime_ShouldReturnCreated() throws Exception {
        Mockito.when(showtimeService.createShowtime(any(ShowtimeRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.movieId").value(10L));
    }

    @Test
    void getShowtime_ShouldReturnShowtime() throws Exception {
        Mockito.when(showtimeService.getShowtime(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/showtimes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.hallId").value(20L));
    }

    @Test
    void updateShowtime_ShouldReturnUpdated() throws Exception {
        ShowtimeResponse updated = new ShowtimeResponse();
        updated.setId(1L);
        updated.setMovieId(10L);
        updated.setHallId(20L);
        updated.setStartTime(LocalDateTime.now().plusDays(1));
        updated.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
        updated.setPrice(200.0);
        updated.setLanguage("FR");
        updated.setFormat("3D");

        Mockito.when(showtimeService.updateShowtime(eq(1L), any(ShowtimeRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(put("/api/v1/showtimes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(200.0))
                .andExpect(jsonPath("$.language").value("FR"));
    }

    @Test
    void deleteShowtime_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(showtimeService).deleteShowtime(1L);

        mockMvc.perform(delete("/api/v1/showtimes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getShowtimesByMovie_ShouldReturnList() throws Exception {
        Mockito.when(showtimeService.getShowtimesByMovie(10L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/showtimes/movies/10/showtimes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(10L));
    }

    @Test
    void filterShowtimes_ShouldReturnFilteredList() throws Exception {
        ShowtimeDTO dto = new ShowtimeDTO();
        dto.setId(1L);
        dto.setMovieId(10L);
        dto.setHallId(20L);
        dto.setLanguage("EN");
        dto.setFormat("IMAX");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(2));
        dto.setPrice(150.0);

        Mockito.when(showtimeService.filterShowtimes(any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/showtimes")
                        .param("movieId", "10")
                        .param("language", "EN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].format").value("IMAX"))
                .andExpect(jsonPath("$[0].movieId").value(10L));
    }
}