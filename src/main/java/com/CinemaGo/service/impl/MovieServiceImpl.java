package com.CinemaGo.service.impl;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.mapper.MovieMapper;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public MovieDTO createMovie(MovieDTO dto) {
        Movie movie = movieMapper.toEntity(dto);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDTO(savedMovie);
    }

    @Override
    public MovieResponseDTO getAllMovies(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> posts =movieRepository.findAll(pageable);
        List<Movie> movies = posts.getContent();
        List<MovieDTO> moviesDTO = movies.stream().map(movieMapper::toDTO).collect(Collectors.toList());
        MovieResponseDTO movieResponseDTO = new MovieResponseDTO();
        movieResponseDTO.setContent(moviesDTO);
        movieResponseDTO.setPageNumber(posts.getNumber());
        movieResponseDTO.setPageSize(posts.getSize());
        movieResponseDTO.setTotalElements(posts.getTotalElements());
        movieResponseDTO.setTotalPages(posts.getTotalPages());
        movieResponseDTO.setLastPage(posts.isLast());
        return movieResponseDTO;
    }

    @Override
    public MovieDTO getMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return movieMapper.toDTO(movie);
    }

    @Override
    public MovieDTO updateMovie(Long id, MovieDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));

        // Instead of manually setting, just map dto → entity (but keep ID + relationships)
        Movie updated = movieMapper.toEntity(dto);
        updated.setId(movie.getId());
        updated.setShowtimes(movie.getShowtimes());

        Movie savedMovie = movieRepository.save(updated);
        return movieMapper.toDTO(savedMovie);
    }

    @Override
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        movieRepository.delete(movie);
    }
    @Override
    public List<Movie> getNowPlayingMovies() {
        LocalDate today = LocalDate.now();
        return movieRepository.findNowPlayingMovies(today);
    }

    @Override
    public List<Movie> getComingSoonMovies() {
        LocalDate today = LocalDate.now();
        return movieRepository.findComingSoonMovies(today);
    }

    @Override
    public void updateMovieAvailability(Long movieId, boolean available) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setAvailable(available);
        movieRepository.save(movie);
    }
}