package com.CinemaGo.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationAdminResponse {

    private Long reservationId;
    private String status;

    private ShowtimeInfo showtime;
    private SeatInfo seat;
    private UserInfo user;

    @Data
    @Builder
    public static class ShowtimeInfo {
        private Long showtimeId;
        private String movieTitle;
        private String hallName;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double price;
        private String language;
        private String format;
    }

    @Data
    @Builder
    public static class SeatInfo {
        private Long seatId;
        private Integer seatNumber;
        private String seatType;
    }

    @Data
    @Builder
    public static class UserInfo {
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
    }
}
