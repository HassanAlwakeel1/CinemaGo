package com.CinemaGo.model.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationResponse {
    private Long reservationId;
    private String status;
    private String message;
}
