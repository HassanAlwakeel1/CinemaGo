package com.CinemaGo.model.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShowtimeResponse {
    private Long id;
    private Long movieId;
    private Long hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private String language;
    private String format;
}
