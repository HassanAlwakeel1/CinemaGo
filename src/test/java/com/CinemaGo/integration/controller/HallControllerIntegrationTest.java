package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.repository.HallRepository;
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
class HallControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HallRepository hallRepository;

    @BeforeEach
    void setup() {
        hallRepository.deleteAll();
    }

    @Test
    void testCreateHall() throws Exception {
        HallRequestDto request = HallRequestDto.builder()
                .name("Hall 1")
                .totalSeats(100)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("Wheelchair")
                .build();

        mockMvc.perform(post("/api/v1/halls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Hall 1")))
                .andExpect(jsonPath("$.totalSeats", is(100)));
    }

    @Test
    void testGetAllHalls() throws Exception {
        hallRepository.save(Hall.builder()
                .name("Hall A")
                .totalSeats(50)
                .seatLayout("VIP")
                .technology("Dolby Atmos")
                .accessibilityFeatures("Wheelchair")
                .build());

        mockMvc.perform(get("/api/v1/halls"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Hall A")));
    }

    @Test
    void testGetHallById() throws Exception {
        Hall saved = hallRepository.save(Hall.builder()
                .name("Hall B")
                .totalSeats(120)
                .seatLayout("Standard")
                .technology("IMAX")
                .accessibilityFeatures("None")
                .build());

        mockMvc.perform(get("/api/v1/halls/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(saved.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Hall B")));
    }

    @Test
    void testUpdateHall() throws Exception {
        Hall saved = hallRepository.save(Hall.builder()
                .name("Old Hall")
                .totalSeats(80)
                .seatLayout("Standard")
                .technology("Dolby")
                .accessibilityFeatures("Wheelchair")
                .build());

        HallRequestDto updateRequest = HallRequestDto.builder()
                .name("Updated Hall")
                .totalSeats(90)
                .seatLayout("VIP")
                .technology("IMAX")
                .accessibilityFeatures("All Features")
                .build();

        mockMvc.perform(put("/api/v1/halls/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Hall")))
                .andExpect(jsonPath("$.totalSeats", is(90)))
                .andExpect(jsonPath("$.seatLayout", is("VIP")))
                .andExpect(jsonPath("$.technology", is("IMAX")))
                .andExpect(jsonPath("$.accessibilityFeatures", is("All Features")));
    }

    @Test
    void testDeleteHall() throws Exception {
        Hall saved = hallRepository.save(Hall.builder()
                .name("Delete Hall")
                .totalSeats(60)
                .seatLayout("Standard")
                .technology("Dolby")
                .accessibilityFeatures("Wheelchair")
                .build());

        // Delete request
        mockMvc.perform(delete("/api/v1/halls/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        // Verify it’s deleted
        mockMvc.perform(get("/api/v1/halls/{id}", saved.getId()))
                .andExpect(status().is4xxClientError()); // Not Found
    }
}
