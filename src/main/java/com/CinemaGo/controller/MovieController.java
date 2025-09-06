package com.CinemaGo.controller;

import com.CinemaGo.model.dto.MovieDTO;
import com.CinemaGo.model.dto.MovieResponseDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.service.MovieService;
import com.CinemaGo.utility.AppConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
@Tag(name = "Movie")
@CrossOrigin
public class MovieController {

    private final MovieService movieService;
    private static final Logger logger = Logger.getLogger(MovieController.class.getName());


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO dto) {
        logger.info("Request to create a new movie: " + dto);
        MovieDTO createdMovie = movieService.createMovie(dto);
        logger.info("Movie created successfully: " + createdMovie.getTitle());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<MovieResponseDTO> getAllMovies(@RequestParam(value = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNumber,
                                                         @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
                                                         @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                         @RequestParam(value = "sortDirection", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDirection){
        logger.info(String.format("Request to get all movies - page: %d, size: %d, sortBy: %s, sortDir: %s",
                pageNumber, pageSize, sortBy, sortDirection));
        MovieResponseDTO movies = movieService.getAllMovies(pageNumber, pageSize, sortBy, sortDirection);
        logger.info("Total movies retrieved: " + movies.getContent().size());
        return ResponseEntity.ok(movies);
    }

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Long id) {
        logger.info("Request to get movie with ID: " + id);
        MovieDTO movie = movieService.getMovie(id);
        logger.info("Movie retrieved: " + movie);
        return ResponseEntity.ok(movie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Long id,
            @RequestBody MovieDTO dto
    ) {
        logger.info("Request to update movie with ID: " + id + " using data: " + dto);
        MovieDTO updatedMovie = movieService.updateMovie(id, dto);
        logger.info("Movie updated successfully: " + updatedMovie.getTitle());
        return ResponseEntity.ok(updatedMovie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        logger.info("Request to delete movie with ID: " + id);
        movieService.deleteMovie(id);
        logger.info("Movie deleted successfully with ID: " + id);
        return ResponseEntity.noContent().build();
    }

    @PermitAll
    @GetMapping("/now-playing")
    public ResponseEntity<List<Movie>> getNowPlayingMovies() {
        logger.info("Request to get now-playing movies");
        List<Movie> movies = movieService.getNowPlayingMovies();
        logger.info("Now-playing movies retrieved: " + movies.size());
        return ResponseEntity.ok(movies);
    }

    @PermitAll
    @GetMapping("/coming-soon")
    public ResponseEntity<List<Movie>> getComingSoonMovies() {
        logger.info("Request to get coming-soon movies");
        List<Movie> movies = movieService.getComingSoonMovies();
        logger.info("Coming-soon movies retrieved: " + movies.size());
        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{movieId}/availability")
    public ResponseEntity<String> updateAvailability(
            @PathVariable Long movieId,
            @RequestParam boolean available
    ) {
        logger.info("Request to update availability for movie ID: " + movieId + " to " + available);
        movieService.updateMovieAvailability(movieId, available);
        logger.info("Movie availability updated successfully for movie ID: " + movieId);
        return ResponseEntity.ok("Movie availability updated successfully");
    }
}
