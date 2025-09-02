package com.CinemaGo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private Long showtimeId;
    private Long seatId;
    private Long userId;
    private Double price;
}
