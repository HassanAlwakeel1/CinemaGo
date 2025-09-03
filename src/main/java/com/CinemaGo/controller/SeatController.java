package com.CinemaGo.controller;

import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;
    private static final Logger logger = Logger.getLogger(SeatController.class.getName());


    /** Create a new Seat */
    @PostMapping
    public ResponseEntity<SeatResponseDto> createSeat(@RequestBody SeatRequestDto dto) {
        try {
            SeatResponseDto created = seatService.createSeat(dto);
            logger.info("Created seat with ID: " + created.getId());
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create seat", e);
            throw e;
        }
    }


    /** Get all seats for a hall */
    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<SeatResponseDto>> getSeatsByHall(@PathVariable Long hallId) {
        try {
            List<SeatResponseDto> seats = seatService.getSeatsByHallId(hallId);
            logger.info("Fetched " + seats.size() + " seats for hall ID: " + hallId);
            return ResponseEntity.ok(seats);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch seats for hall ID: " + hallId, e);
            throw e;
        }
    }

    /** Get a seat by ID */
    @GetMapping("/{id}")
    public ResponseEntity<SeatResponseDto> getSeatById(@PathVariable Long id) {
        try {
            SeatResponseDto seat = seatService.getSeatById(id);
            logger.info("Fetched seat with ID: " + id);
            return ResponseEntity.ok(seat);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch seat with ID: " + id, e);
            throw e;
        }
    }

    /** Update a seat by ID */
    @PutMapping("/{id}")
    public ResponseEntity<SeatResponseDto> updateSeat(
            @PathVariable Long id,
            @RequestBody SeatRequestDto dto
    ) {
        try {
            SeatResponseDto updatedSeat = seatService.updateSeat(id, dto);
            logger.info("Updated seat with ID: " + id);
            return ResponseEntity.ok(updatedSeat);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update seat with ID: " + id, e);
            throw e;
        }
    }

    /** Delete a seat by ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        try {
            seatService.deleteSeat(id);
            logger.info("Deleted seat with ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete seat with ID: " + id, e);
            throw e;
        }
    }
}