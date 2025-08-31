package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntegrationTesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        movieRepository.deleteAll(); // clean DB before each test
    }

    @Test
    void testCreateMovie() throws Exception {
        MovieDTO dto = MovieDTO.builder()
                .title("Inception")
                .description("Dream heist thriller")
                .genre("Sci-Fi")
                .posterUrl("poster.jpg")
                .durationMinutes(148)
                .rating("PG-13")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .language("English")
                .subtitlesAvailable(true)
                .subtitleLanguage("Spanish")
                .build();

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Inception")))
                .andExpect(jsonPath("$.genre", is("Sci-Fi")));
    }

    @Test
    void testGetAllMovies() throws Exception {
        // Seed DB
        movieRepository.save(com.CinemaGo.model.entity.Movie.builder()
                .title("Avatar")
                .description("Epic sci-fi")
                .genre("Adventure")
                .durationMinutes(162)
                .rating("PG-13")
                .releaseDate(LocalDate.of(2009, 12, 18))
                .language("English")
                .build());

        mockMvc.perform(get("/api/v1/movies")
                        .param("pageNumber", "0")
                        .param("pageSize", "5")
                        .param("sortBy", "id")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Avatar")));
    }

    @Test
    void testGetMovieById() throws Exception {
        var saved = movieRepository.save(com.CinemaGo.model.entity.Movie.builder()
                .title("Interstellar")
                .description("Space travel")
                .genre("Sci-Fi")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .build());

        mockMvc.perform(get("/api/v1/movies/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar")));
    }

    @Test
    void testUpdateMovie() throws Exception {
        var saved = movieRepository.save(com.CinemaGo.model.entity.Movie.builder()
                .title("Old Title")
                .genre("Drama")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build());

        MovieDTO updateDto = MovieDTO.builder()
                .title("New Title")
                .description("Updated description")
                .genre("Sci-Fi")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .build();

        mockMvc.perform(put("/api/v1/movies/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.description", is("Updated description")));
    }

    @Test
    void testDeleteMovie() throws Exception {
        var saved = movieRepository.save(com.CinemaGo.model.entity.Movie.builder()
                .title("Delete Me")
                .genre("Horror")
                .build());

        mockMvc.perform(delete("/api/v1/movies/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/movies/{id}", saved.getId()))
                .andExpect(status().is4xxClientError()); // movie not found
    }
}

