package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.enums.SeatType;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.repository.SeatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SeatControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private SeatRepository seatRepository;

    private Hall hall;

    @BeforeEach
    void setup() {
        seatRepository.deleteAll();
        hallRepository.deleteAll();

        hall = hallRepository.save(Hall.builder()
                .name("Main Hall")
                .totalSeats(100)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("Wheelchair")
                .build());
    }

    @Test
    void testCreateSeat() throws Exception {
        SeatRequestDto request = SeatRequestDto.builder()
                .seatNumber(1)
                .seatType(SeatType.STANDARD)
                .hallId(hall.getId())
                .build();

        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber", is(1)))
                .andExpect(jsonPath("$.seatType", is("STANDARD")));
    }

    @Test
    void testCreateDuplicateSeat_returnsError() throws Exception {
        SeatRequestDto request = SeatRequestDto.builder()
                .seatNumber(1)
                .seatType(SeatType.STANDARD)
                .hallId(hall.getId())
                .build();

        // Create the first seat
        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Try to create duplicate seat
        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("already exists")));
    }

    @Test
    void testGetSeatsByHall() throws Exception {
        SeatRequestDto request1 = SeatRequestDto.builder()
                .seatNumber(1)
                .seatType(SeatType.STANDARD)
                .hallId(hall.getId())
                .build();

        SeatRequestDto request2 = SeatRequestDto.builder()
                .seatNumber(2)
                .seatType(SeatType.VIP)
                .hallId(hall.getId())
                .build();

        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/seats/hall/{hallId}", hall.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].seatNumber", is(1)))
                .andExpect(jsonPath("$[1].seatNumber", is(2)));
    }

    @Test
    void testUpdateSeat() throws Exception {
        SeatRequestDto createRequest = SeatRequestDto.builder()
                .seatNumber(1)
                .seatType(SeatType.STANDARD)
                .hallId(hall.getId())
                .build();

        String content = mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long seatId = objectMapper.readTree(content).get("id").asLong();

        SeatRequestDto updateRequest = SeatRequestDto.builder()
                .seatNumber(10)
                .seatType(SeatType.VIP)
                .hallId(hall.getId())
                .build();

        mockMvc.perform(put("/api/v1/seats/{id}", seatId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber", is(10)))
                .andExpect(jsonPath("$.seatType", is("VIP")));
    }

    @Test
    void testDeleteSeat() throws Exception {
        SeatRequestDto request = SeatRequestDto.builder()
                .seatNumber(1)
                .seatType(SeatType.STANDARD)
                .hallId(hall.getId())
                .build();

        String content = mockMvc.perform(post("/api/v1/seats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long seatId = objectMapper.readTree(content).get("id").asLong();

        mockMvc.perform(delete("/api/v1/seats/{id}", seatId))
                .andExpect(status().isNoContent());

        // Verify deleted
        mockMvc.perform(get("/api/v1/seats/{id}", seatId))
                .andExpect(status().is4xxClientError());
    }
}

