package com.CinemaGo.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowtimeDTO {
    private Long id;

    private Long movieId;
    private String movieTitle; // helpful for responses

    private Long hallId;
    private String hallName;   // helpful for responses

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
    private String language;
    private String format;
}