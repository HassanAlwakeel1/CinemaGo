package com.CinemaGo.controller;

import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;
import com.CinemaGo.service.MovieCrewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-crew")
@RequiredArgsConstructor
public class MovieCrewController {

    private final MovieCrewService movieCrewService;

    @PostMapping
    public ResponseEntity<MovieCrewResponseDTO> addPersonToMovie(@RequestBody MovieCrewRequestDTO dto) {
        return ResponseEntity.ok(movieCrewService.addPersonToMovie(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieCrewResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(movieCrewService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovieCrewResponseDTO>> getAll() {
        return ResponseEntity.ok(movieCrewService.getAll());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<MovieCrewResponseDTO>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(movieCrewService.getByMovie(movieId));
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<List<MovieCrewResponseDTO>> getByPerson(@PathVariable Long personId) {
        return ResponseEntity.ok(movieCrewService.getByPerson(personId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieCrewResponseDTO> update(@PathVariable Long id,
                                                       @RequestBody MovieCrewRequestDTO dto) {
        return ResponseEntity.ok(movieCrewService.updateMovieCrew(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieCrewService.removeFromMovie(id);
        return ResponseEntity.noContent().build();
    }
}