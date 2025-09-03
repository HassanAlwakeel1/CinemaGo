package com.CinemaGo.controller;

import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;
import com.CinemaGo.service.ShowtimeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtimes", description = "CRUD operations for movie showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;
    private static final Logger logger = Logger.getLogger(ShowtimeController.class.getName());


    // Create
    @PostMapping
    public ResponseEntity<ShowtimeResponse> createShowtime(@RequestBody ShowtimeRequest request) {
        try {
            ShowtimeResponse response = showtimeService.createShowtime(request);
            logger.info("Created showtime for movie ID: " + request.getMovieId() +
                    " in hall ID: " + request.getHallId() +
                    " at: " + request.getStartTime());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create showtime for movie ID: " + request.getMovieId(), e);
            throw e;
        }
    }

    // Read single
    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtime(@PathVariable Long id) {
        try {
            ShowtimeResponse response = showtimeService.getShowtime(id);
            logger.info("Fetched showtime with ID: " + id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch showtime with ID: " + id, e);
            throw e;
        }
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> updateShowtime(@PathVariable Long id,
                                                           @RequestBody ShowtimeRequest request) {
        try {
            ShowtimeResponse response = showtimeService.updateShowtime(id, request);
            logger.info("Updated showtime ID: " + id +
                    " with new start time: " + request.getStartTime());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update showtime ID: " + id, e);
            throw e;
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        try {
            showtimeService.deleteShowtime(id);
            logger.info("Deleted showtime with ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete showtime with ID: " + id, e);
            throw e;
        }
    }

    @GetMapping("/movies/{id}/showtimes")
    public ResponseEntity<List<ShowtimeResponse>> getShowtimesByMovie(@PathVariable Long id) {
        try {
            List<ShowtimeResponse> showtimes = showtimeService.getShowtimesByMovie(id);
            logger.info("Fetched " + showtimes.size() + " showtimes for movie ID: " + id);
            return ResponseEntity.ok(showtimes);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch showtimes for movie ID: " + id, e);
            throw e;
        }
    }

    @GetMapping
    public List<ShowtimeDTO> filterShowtimes(
            @RequestParam(required = false) Long movieId,
            @RequestParam(required = false) Long hallId,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String format,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endBefore
    ) {
        try {
            List<ShowtimeDTO> showtimes = showtimeService.filterShowtimes(movieId, hallId, language, format, startAfter, endBefore);
            logger.info("Filtered showtimes with params -> movieId: " + movieId +
                    ", hallId: " + hallId + ", language: " + language + ", format: " + format);
            return showtimes;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to filter showtimes", e);
            throw e;
        }
    }
}