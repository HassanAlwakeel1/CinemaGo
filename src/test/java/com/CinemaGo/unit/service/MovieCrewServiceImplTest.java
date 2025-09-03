package com.CinemaGo.unit.service;


import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.entity.MovieCrew;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.model.entity.enums.CrewRole;
import com.CinemaGo.repository.MovieCrewRepository;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.repository.PersonRepository;
import com.CinemaGo.service.impl.MovieCrewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieCrewServiceImplTest {

    @Mock
    private MovieCrewRepository movieCrewRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private MovieCrewServiceImpl movieCrewService;

    private Movie movie;
    private Person person;
    private MovieCrew crew;
    private MovieCrewRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        movie = Movie.builder().id(1L).title("Inception").build();
        person = Person.builder().id(2L).firstName("Leonardo").lastName("DiCaprio").build();
        crew = MovieCrew.builder()
                .id(10L)
                .movie(movie)
                .person(person)
                .role(CrewRole.ACTOR)
                .characterName("Cobb")
                .build();

        requestDTO = new MovieCrewRequestDTO();
        requestDTO.setMovieId(1L);
        requestDTO.setPersonId(2L);
        requestDTO.setRole(CrewRole.ACTOR);
        requestDTO.setCharacterName("Cobb");
    }

    @Test
    void addPersonToMovie_ShouldSaveAndReturnDTO() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(personRepository.findById(2L)).thenReturn(Optional.of(person));
        when(movieCrewRepository.save(any(MovieCrew.class))).thenReturn(crew);

        MovieCrewResponseDTO response = movieCrewService.addPersonToMovie(requestDTO);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Inception", response.getMovieTitle());
        assertEquals("Leonardo DiCaprio", response.getPersonName());
        verify(movieRepository).findById(1L);
        verify(personRepository).findById(2L);
        verify(movieCrewRepository).save(any(MovieCrew.class));
    }

    @Test
    void addPersonToMovie_ShouldThrow_WhenMovieNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movieCrewService.addPersonToMovie(requestDTO));

        assertTrue(ex.getMessage().contains("Movie not found"));
    }

    @Test
    void addPersonToMovie_ShouldThrow_WhenPersonNotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(personRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movieCrewService.addPersonToMovie(requestDTO));

        assertTrue(ex.getMessage().contains("Person not found"));
    }

    @Test
    void getById_ShouldReturnDTO() {
        when(movieCrewRepository.findById(10L)).thenReturn(Optional.of(crew));

        MovieCrewResponseDTO response = movieCrewService.getById(10L);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Cobb", response.getCharacterName());
    }

    @Test
    void getById_ShouldThrow_WhenNotFound() {
        when(movieCrewRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movieCrewService.getById(10L));

        assertTrue(ex.getMessage().contains("MovieCrew not found"));
    }

    @Test
    void getAll_ShouldReturnList() {
        when(movieCrewRepository.findAll()).thenReturn(List.of(crew));

        List<MovieCrewResponseDTO> result = movieCrewService.getAll();

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getMovieTitle());
    }

    @Test
    void getByMovie_ShouldReturnCrewList() {
        when(movieCrewRepository.findByMovieId(1L)).thenReturn(List.of(crew));

        List<MovieCrewResponseDTO> result = movieCrewService.getByMovie(1L);

        assertEquals(1, result.size());
        assertEquals("Cobb", result.get(0).getCharacterName());
    }

    @Test
    void getByPerson_ShouldReturnMovieList() {
        when(movieCrewRepository.findByPersonId(2L)).thenReturn(List.of(crew));

        List<MovieCrewResponseDTO> result = movieCrewService.getByPerson(2L);

        assertEquals(1, result.size());
        assertEquals("Leonardo DiCaprio", result.get(0).getPersonName());
    }

    @Test
    void updateMovieCrew_ShouldUpdateRoleAndCharacterName() {
        MovieCrewRequestDTO updateDTO = new MovieCrewRequestDTO();
        updateDTO.setRole(CrewRole.DIRECTOR);
        updateDTO.setCharacterName("N/A");

        when(movieCrewRepository.findById(10L)).thenReturn(Optional.of(crew));
        when(movieCrewRepository.save(any(MovieCrew.class))).thenReturn(crew);

        MovieCrewResponseDTO response = movieCrewService.updateMovieCrew(10L, updateDTO);

        assertEquals(CrewRole.DIRECTOR, response.getRole());
        assertEquals("N/A", response.getCharacterName());
        verify(movieCrewRepository).save(any(MovieCrew.class));
    }

    @Test
    void updateMovieCrew_ShouldThrow_WhenNotFound() {
        when(movieCrewRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movieCrewService.updateMovieCrew(10L, requestDTO));

        assertTrue(ex.getMessage().contains("MovieCrew not found"));
    }

    @Test
    void removeFromMovie_ShouldDeleteCrew() {
        when(movieCrewRepository.findById(10L)).thenReturn(Optional.of(crew));
        doNothing().when(movieCrewRepository).delete(crew);

        movieCrewService.removeFromMovie(10L);

        verify(movieCrewRepository).delete(crew);
    }

    @Test
    void removeFromMovie_ShouldThrow_WhenNotFound() {
        when(movieCrewRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> movieCrewService.removeFromMovie(10L));

        assertTrue(ex.getMessage().contains("MovieCrew not found"));
    }
}
