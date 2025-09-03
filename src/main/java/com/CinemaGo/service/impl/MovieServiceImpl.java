package com.CinemaGo.service.impl;

import com.CinemaGo.exception.ResourceNotFoundException;
import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.mapper.MovieMapper;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieServiceImpl.class);

    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    //CachePut responsible of Caching the result of this method
    //key = "#result.id" is used to cache the result of this method
    //يعني ايه ال #result.id؟
    @Override
    @CachePut(value = "MOVIE_CACHE",key = "#result.id()") //CachePut responsible of Caching the result of this method
    public MovieDTO createMovie(MovieDTO dto) {
        LOGGER.info("Creating movie {}", dto);
        Movie movie = movieMapper.toEntity(dto);
        Movie savedMovie = movieRepository.save(movie);
        LOGGER.info("Movie created: {}", savedMovie);
        return movieMapper.toDTO(savedMovie);
    }

    @Override
    public MovieResponseDTO getAllMovies(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        LOGGER.info("Fetching all movies. Page number: {}, page size: {}, sort by: {}, sort direction: {}",
                pageNumber, pageSize, sortBy, sortDirection);
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Movie> posts = movieRepository.findAll(pageable);
        List<Movie> movies = posts.getContent();
        List<MovieDTO> moviesDTO = movies.stream().map(movieMapper::toDTO).collect(Collectors.toList());
        MovieResponseDTO movieResponseDTO = new MovieResponseDTO();
        movieResponseDTO.setContent(moviesDTO);
        movieResponseDTO.setPageNumber(posts.getNumber());
        movieResponseDTO.setPageSize(posts.getSize());
        movieResponseDTO.setTotalElements(posts.getTotalElements());
        movieResponseDTO.setTotalPages(posts.getTotalPages());
        movieResponseDTO.setLastPage(posts.isLast());
        LOGGER.info("Fetched all movies. Total movies: {}", movieResponseDTO.getTotalElements());
        return movieResponseDTO;
    }

    @Override
    @Cacheable(value = "MOVIE_CACHE",key = "#id")
    public MovieDTO getMovie(Long id) {
        LOGGER.info("Fetching movie with id {}", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        LOGGER.info("Movie fetched: {}", movie);
        return movieMapper.toDTO(movie);
    }

    @Override
    @CachePut(value = "MOVIE_CACHE",key = "#result.id()")
    public MovieDTO updateMovie(Long id, MovieDTO dto) {
        LOGGER.info("Updating movie with id {}", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));

        // Instead of manually setting, just map dto → entity (but keep ID + relationships)
        Movie updated = movieMapper.toEntity(dto);
        updated.setId(movie.getId());
        updated.setShowtimes(movie.getShowtimes());

        Movie savedMovie = movieRepository.save(updated);
        LOGGER.info("Movie updated: {}", savedMovie);
        return movieMapper.toDTO(savedMovie);
    }

    @Override
    @CacheEvict(value = "MOVIE_CACHE",key = "#id")
    public void deleteMovie(Long id) {
        LOGGER.info("Deleting movie with id {}", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        movieRepository.delete(movie);
        LOGGER.info("Movie deleted: {}", movie);
    }

    @Override
    public List<Movie> getNowPlayingMovies() {
        LOGGER.info("Fetching now playing movies");
        LocalDate today = LocalDate.now();
        return movieRepository.findNowPlayingMovies(today);
    }

    @Override
    public List<Movie> getComingSoonMovies() {
        LOGGER.info("Fetching coming soon movies");
        LocalDate today = LocalDate.now();
        return movieRepository.findComingSoonMovies(today);
    }

    @Override
    public void updateMovieAvailability(Long movieId, boolean available) {
        LOGGER.info("Updating movie availability for movie with id {}", movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movie.setAvailable(available);
        movieRepository.save(movie);
        LOGGER.info("Movie availability updated for movie with id {}", movieId);
    }
}