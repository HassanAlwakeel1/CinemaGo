package com.CinemaGo.model.dto;

import com.CinemaGo.model.entity.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatRequestDto {
    private Integer seatNumber;
    private SeatType seatType;
    private Long hallId;
}