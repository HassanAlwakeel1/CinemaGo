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

@RestController
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
@Tag(name = "Showtimes", description = "CRUD operations for movie showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    // Create
    @PostMapping
    public ResponseEntity<ShowtimeResponse> createShowtime(@RequestBody ShowtimeRequest request) {
        ShowtimeResponse response = showtimeService.createShowtime(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Read single
    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtime(@PathVariable Long id) {
        ShowtimeResponse response = showtimeService.getShowtime(id);
        return ResponseEntity.ok(response);
    }

    // Read all
//    @GetMapping
//    public ResponseEntity<List<ShowtimeResponse>> getAllShowtimes() {
//        List<ShowtimeResponse> responses = showtimeService.getAllShowtimes();
//        return ResponseEntity.ok(responses);
//    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> updateShowtime(@PathVariable Long id,
                                                           @RequestBody ShowtimeRequest request) {
        ShowtimeResponse response = showtimeService.updateShowtime(id, request);
        return ResponseEntity.ok(response);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movies/{id}/showtimes")
    public ResponseEntity<List<ShowtimeResponse>> getShowtimesByMovie(@PathVariable Long id) {
        List<ShowtimeResponse> showtimes = showtimeService.getShowtimesByMovie(id);
        return ResponseEntity.ok(showtimes);
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
        return showtimeService.filterShowtimes(movieId, hallId, language, format, startAfter, endBefore);
    }
}
