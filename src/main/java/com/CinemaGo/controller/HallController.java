package com.CinemaGo.controller;

import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    /** Create a new Hall */
    @PostMapping
    public ResponseEntity<HallResponseDto> createHall(@RequestBody HallRequestDto hallRequestDto) {
        HallResponseDto createdHall = hallService.createHall(hallRequestDto);
        return ResponseEntity.ok(createdHall);
    }

    /** Get Hall by ID */
    @GetMapping("/{id}")
    public ResponseEntity<HallResponseDto> getHallById(@PathVariable Long id) {
        HallResponseDto hall = hallService.getHallById(id);
        return ResponseEntity.ok(hall);
    }

    /** Get all Halls */
    @GetMapping
    public ResponseEntity<List<HallResponseDto>> getAllHalls() {
        List<HallResponseDto> halls = hallService.getAllHalls();
        return ResponseEntity.ok(halls);
    }

    /** Update a Hall */
    @PutMapping("/{id}")
    public ResponseEntity<HallResponseDto> updateHall(
            @PathVariable Long id,
            @RequestBody HallRequestDto hallRequestDto
    ) {
        HallResponseDto updatedHall = hallService.updateHall(id, hallRequestDto);
        return ResponseEntity.ok(updatedHall);
    }

    /** Delete a Hall */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }
}
