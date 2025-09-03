package com.CinemaGo.model.entity.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CrewRole {
    DIRECTOR("Director"),
    WRITER("Writer"),
    ACTOR("Actor");

    private final String displayName;

    CrewRole(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
