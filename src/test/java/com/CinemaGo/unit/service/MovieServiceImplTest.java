package com.CinemaGo.unit.service;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.mapper.MovieMapper;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .id(1L)
                .title("Inception")
                .description("A mind-bending thriller")
                .genre("Sci-Fi")
                .posterUrl("poster.jpg")
                .durationMinutes(148)
                .rating("PG-13")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .language("English")
                .subtitlesAvailable(true)
                .subtitleLanguage("Spanish")
                .build();

        movieDTO = MovieDTO.builder()
                .title("Inception")
                .description("A mind-bending thriller")
                .genre("Sci-Fi")
                .posterUrl("poster.jpg")
                .durationMinutes(148)
                .rating("PG-13")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .language("English")
                .subtitlesAvailable(true)
                .subtitleLanguage("Spanish")
                .build();
    }

    @Test
    void createMovie_ShouldReturnSavedMovieDTO() {
        given(movieMapper.toEntity(movieDTO)).willReturn(movie);
        given(movieRepository.save(movie)).willReturn(movie);
        given(movieMapper.toDTO(movie)).willReturn(movieDTO);

        MovieDTO result = movieService.createMovie(movieDTO);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        then(movieRepository).should().save(movie);
    }

    @Test
    void getAllMovies_ShouldReturnPagedResponse() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));

        given(movieRepository.findAll(pageable)).willReturn(moviePage);
        given(movieMapper.toDTO(movie)).willReturn(movieDTO);

        MovieResponseDTO response = movieService.getAllMovies(0, 10, "title", "ASC");

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("Inception", response.getContent().get(0).getTitle());
    }

    @Test
    void getMovie_ShouldReturnMovieDTO_WhenExists() {
        given(movieRepository.findById(1L)).willReturn(Optional.of(movie));
        given(movieMapper.toDTO(movie)).willReturn(movieDTO);

        MovieDTO result = movieService.getMovie(1L);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    void getMovie_ShouldThrowException_WhenNotFound() {
        given(movieRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> movieService.getMovie(99L));
    }

    @Test
    void updateMovie_ShouldReturnUpdatedMovieDTO_WhenExists() {
        Movie updatedMovie = Movie.builder()
                .id(1L)
                .title("Inception Updated")
                .description("Updated description")
                .build();

        MovieDTO updatedDTO = MovieDTO.builder()
                .title("Inception Updated")
                .description("Updated description")
                .build();

        given(movieRepository.findById(1L)).willReturn(Optional.of(movie));
        given(movieMapper.toEntity(updatedDTO)).willReturn(updatedMovie);
        given(movieRepository.save(updatedMovie)).willReturn(updatedMovie);
        given(movieMapper.toDTO(updatedMovie)).willReturn(updatedDTO);

        MovieDTO result = movieService.updateMovie(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Inception Updated", result.getTitle());
    }

    @Test
    void updateMovie_ShouldThrowException_WhenNotFound() {
        given(movieRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> movieService.updateMovie(99L, movieDTO));
    }

    @Test
    void deleteMovie_ShouldCallRepositoryDelete() {
        willDoNothing().given(movieRepository).deleteById(1L);

        movieService.deleteMovie(1L);

        then(movieRepository).should().deleteById(1L);
    }
}