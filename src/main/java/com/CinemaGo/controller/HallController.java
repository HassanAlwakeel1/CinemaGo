package com.CinemaGo.controller;

import com.CinemaGo.model.dto.HallRequestDto;
import com.CinemaGo.model.dto.HallResponseDto;
import com.CinemaGo.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;
    private static final Logger logger = Logger.getLogger(HallController.class.getName());


    /** Create a new Hall */
    @PostMapping
    public ResponseEntity<HallResponseDto> createHall(@RequestBody HallRequestDto hallRequestDto) {
        logger.info("Request to create a new hall: " + hallRequestDto);
        HallResponseDto createdHall = hallService.createHall(hallRequestDto);
        logger.info("Hall created successfully with ID: " + createdHall.getId());
        return ResponseEntity.ok(createdHall);
    }

    /** Get Hall by ID */
    @GetMapping("/{id}")
    public ResponseEntity<HallResponseDto> getHallById(@PathVariable Long id) {
        logger.info("Request to get hall with ID: " + id);
        HallResponseDto hall = hallService.getHallById(id);
        logger.info("Hall retrieved: " + hall);
        return ResponseEntity.ok(hall);
    }

    /** Get all Halls */
    @GetMapping
    public ResponseEntity<List<HallResponseDto>> getAllHalls() {
        logger.info("Request to get all halls");
        List<HallResponseDto> halls = hallService.getAllHalls();
        logger.info("Total halls retrieved: " + halls.size());
        return ResponseEntity.ok(halls);
    }

    /** Update a Hall */
    @PutMapping("/{id}")
    public ResponseEntity<HallResponseDto> updateHall(
            @PathVariable Long id,
            @RequestBody HallRequestDto hallRequestDto
    ) {
        logger.info("Request to update hall with ID: " + id + " using data: " + hallRequestDto);
        HallResponseDto updatedHall = hallService.updateHall(id, hallRequestDto);
        logger.info("Hall updated successfully with ID: " + updatedHall.getId());
        return ResponseEntity.ok(updatedHall);
    }

    /** Delete a Hall */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        logger.info("Request to delete hall with ID: " + id);
        hallService.deleteHall(id);
        logger.info("Hall deleted successfully with ID: " + id);
        return ResponseEntity.noContent().build();
    }
}
