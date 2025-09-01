package com.CinemaGo.model.dto;

import lombok.Data;

@Data
public class ReservationRequest {
    private Long showtimeId;
    private Long seatId;
    private Long userId;
    private Double price;
}
