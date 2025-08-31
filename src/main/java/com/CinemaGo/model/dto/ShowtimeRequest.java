package com.CinemaGo.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeRequest {
    private Long movieId;
    private Long hallId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private String language;
    private String format;
}
