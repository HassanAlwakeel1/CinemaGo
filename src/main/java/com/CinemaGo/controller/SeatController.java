package com.CinemaGo.controller;

import com.CinemaGo.model.dto.SeatRequestDto;
import com.CinemaGo.model.dto.SeatResponseDto;
import com.CinemaGo.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    /** Create a new Seat */
    @PostMapping
    public ResponseEntity<SeatResponseDto> createSeat(@RequestBody SeatRequestDto dto) {
        SeatResponseDto created = seatService.createSeat(dto);
        return ResponseEntity.ok(created);
    }

    /** Get all seats for a hall */
    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<SeatResponseDto>> getSeatsByHall(@PathVariable Long hallId) {
        List<SeatResponseDto> seats = seatService.getSeatsByHallId(hallId);
        return ResponseEntity.ok(seats);
    }

    /** Get a seat by ID */
    @GetMapping("/{id}")
    public ResponseEntity<SeatResponseDto> getSeatById(@PathVariable Long id) {
        SeatResponseDto seat = seatService.getSeatById(id);
        return ResponseEntity.ok(seat);
    }

    /** Update a seat by ID */
    @PutMapping("/{id}")
    public ResponseEntity<SeatResponseDto> updateSeat(
            @PathVariable Long id,
            @RequestBody SeatRequestDto dto
    ) {
        SeatResponseDto updatedSeat = seatService.updateSeat(id, dto);
        return ResponseEntity.ok(updatedSeat);
    }

    /** Delete a seat by ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}
