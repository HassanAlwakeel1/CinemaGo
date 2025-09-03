package com.CinemaGo.unit.service;

import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.model.entity.Hall;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.entity.Showtime;
import com.CinemaGo.model.mapper.ShowtimeMapper;
import com.CinemaGo.repository.HallRepository;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.repository.ShowtimeRepository;
import com.CinemaGo.service.impl.ShowtimeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ShowtimeServiceImplTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private HallRepository hallRepository;

    @Mock
    private ShowtimeMapper showtimeMapper;

    @InjectMocks
    private ShowtimeServiceImpl showtimeService;

    private Movie movie;
    private Hall hall;
    private Showtime showtime;
    private ShowtimeRequest request;
    private ShowtimeResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Inception");

        hall = new Hall();
        hall.setId(1L);
        hall.setName("Main Hall");

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setHall(hall);
        showtime.setStartTime(LocalDateTime.now());
        showtime.setEndTime(LocalDateTime.now().plusHours(2));
        showtime.setLanguage("English");
        showtime.setFormat("2D");
        showtime.setPrice(100.0);

        request = new ShowtimeRequest();
        request.setMovieId(1L);
        request.setHallId(1L);
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusHours(2));
        request.setLanguage("English");
        request.setFormat("2D");
        request.setPrice(100.0);

        response = new ShowtimeResponse();
        response.setId(1L);
        response.setMovieId(1L);
        response.setHallId(1L);
        response.setStartTime(request.getStartTime());
        response.setEndTime(request.getEndTime());
        response.setLanguage("English");
        response.setFormat("2D");
        response.setPrice(100.0);
    }

    @Test
    void createShowtime_ShouldReturnResponse() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(showtimeMapper.toEntity(any(ShowtimeRequest.class))).thenReturn(showtime);
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);
        when(showtimeMapper.toResponse(showtime)).thenReturn(response);

        ShowtimeResponse result = showtimeService.createShowtime(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(showtimeRepository, times(1)).save(showtime);
    }

    @Test
    void createShowtime_MovieNotFound_ShouldThrowException() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> showtimeService.createShowtime(request));
    }

    @Test
    void getShowtime_ExistingId_ShouldReturnResponse() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(showtimeMapper.toResponse(showtime)).thenReturn(response);

        ShowtimeResponse result = showtimeService.getShowtime(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getShowtime_NonExistingId_ShouldThrowException() {
        when(showtimeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> showtimeService.getShowtime(99L));
    }

    @Test
    void getAllShowtimes_ShouldReturnList() {
        when(showtimeRepository.findAll()).thenReturn(Arrays.asList(showtime));
        when(showtimeMapper.toResponse(showtime)).thenReturn(response);

        List<ShowtimeResponse> result = showtimeService.getAllShowtimes();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void updateShowtime_ShouldReturnUpdatedResponse() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(hallRepository.findById(1L)).thenReturn(Optional.of(hall));
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(showtime);
        when(showtimeMapper.toResponse(showtime)).thenReturn(response);

        ShowtimeResponse result = showtimeService.updateShowtime(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateShowtime_NonExistingId_ShouldThrowException() {
        when(showtimeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> showtimeService.updateShowtime(99L, request));
    }

    @Test
    void deleteShowtime_ExistingId_ShouldDelete() {
        when(showtimeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(showtimeRepository).deleteById(1L);

        showtimeService.deleteShowtime(1L);

        verify(showtimeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteShowtime_NonExistingId_ShouldDoNothing() {
        when(showtimeRepository.existsById(99L)).thenReturn(false);

        showtimeService.deleteShowtime(99L);

        verify(showtimeRepository, times(1)).deleteById(99L);
    }

    @Test
    void getShowtimesByMovie_ShouldReturnList() {
        when(showtimeRepository.findByMovieId(1L)).thenReturn(Arrays.asList(showtime));
        when(showtimeMapper.toResponse(showtime)).thenReturn(response);

        List<ShowtimeResponse> result = showtimeService.getShowtimesByMovie(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMovieId());
    }

    @Test
    void filterShowtimes_ShouldReturnFilteredList() {
        when(showtimeRepository.findAll()).thenReturn(Arrays.asList(showtime));
        when(showtimeMapper.toDto(showtime)).thenReturn(new ShowtimeDTO());

        List<ShowtimeDTO> result = showtimeService.filterShowtimes(
                1L, 1L, "English", "2D",
                LocalDateTime.now().minusHours(1),
                LocalDateTime.now().plusHours(3));

        assertEquals(1, result.size());
    }
}
