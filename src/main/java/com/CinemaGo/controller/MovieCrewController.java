package com.CinemaGo.controller;

import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;
import com.CinemaGo.service.MovieCrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/movie-crew")
@RequiredArgsConstructor
public class MovieCrewController {

    private final MovieCrewService movieCrewService;
    private static final Logger logger = Logger.getLogger(MovieCrewController.class.getName());

    @PostMapping
    public ResponseEntity<MovieCrewResponseDTO> addPersonToMovie(@RequestBody MovieCrewRequestDTO dto) {
        MovieCrewResponseDTO response = movieCrewService.addPersonToMovie(dto);
        logger.info("Added person to movie: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieCrewResponseDTO> getById(@PathVariable Long id) {
        MovieCrewResponseDTO response = movieCrewService.getById(id);
        logger.info("Retrieved movie crew with ID: " + id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MovieCrewResponseDTO>> getAll() {
        List<MovieCrewResponseDTO> response = movieCrewService.getAll();
        logger.info("Retrieved all movie crew entries, total: " + response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<MovieCrewResponseDTO>> getByMovie(@PathVariable Long movieId) {
        List<MovieCrewResponseDTO> response = movieCrewService.getByMovie(movieId);
        logger.info("Retrieved movie crew for movie ID: " + movieId + ", total: " + response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<MovieCrewResponseDTO>> getByPerson(@PathVariable Long personId) {
        List<MovieCrewResponseDTO> response = movieCrewService.getByPerson(personId);
        logger.info("Retrieved movie crew for person ID: " + personId + ", total: " + response.size());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieCrewResponseDTO> update(@PathVariable Long id,
                                                       @RequestBody MovieCrewRequestDTO dto) {
        MovieCrewResponseDTO response = movieCrewService.updateMovieCrew(id, dto);
        logger.info("Updated movie crew with ID: " + id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            movieCrewService.removeFromMovie(id);
            logger.info("Deleted movie crew with ID: " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete movie crew with ID: " + id, e);
            throw e;
        }
        return ResponseEntity.noContent().build();
    }
}