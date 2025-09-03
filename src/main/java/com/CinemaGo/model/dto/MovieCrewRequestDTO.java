package com.CinemaGo.model.dto;

import com.CinemaGo.model.entity.enums.CrewRole;
import lombok.Data;

@Data
public class MovieCrewRequestDTO {
    private Long movieId;
    private Long personId;
    private CrewRole role;
    private String characterName; // optional, only for actors
}
