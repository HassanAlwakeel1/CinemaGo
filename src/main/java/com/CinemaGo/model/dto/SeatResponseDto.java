package com.CinemaGo.model.dto;

import com.CinemaGo.model.entity.enums.SeatType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatResponseDto {
    private Long id;
    private Integer seatNumber;
    private SeatType seatType;
    private Long hallId;
}
