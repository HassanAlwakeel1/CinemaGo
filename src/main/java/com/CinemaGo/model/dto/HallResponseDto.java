package com.CinemaGo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HallResponseDto {
    private Long id;
    private String name;
    private Integer totalSeats;
    private String seatLayout;
    private String technology;
    private String accessibilityFeatures;
}
