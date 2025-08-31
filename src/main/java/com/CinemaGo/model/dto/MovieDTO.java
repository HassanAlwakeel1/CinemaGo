package com.CinemaGo.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private String title;
    private String description;
    private String genre;
    private String posterUrl;
    private Integer durationMinutes;
    private String rating;
    private LocalDate releaseDate;
    private String language;
    private Boolean subtitlesAvailable;
    private String subtitleLanguage;
}
