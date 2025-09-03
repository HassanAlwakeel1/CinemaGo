package com.CinemaGo.model.dto;

import com.CinemaGo.model.entity.enums.CrewRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieCrewResponseDTO {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long personId;
    private String personName;
    private CrewRole role;
    private String characterName;
}