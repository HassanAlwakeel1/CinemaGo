package com.CinemaGo.service;


import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;

import java.util.List;

public interface ShowtimeService {
    ShowtimeResponse createShowtime(ShowtimeRequest request);
    ShowtimeResponse getShowtime(Long id);
    List<ShowtimeResponse> getAllShowtimes();
    ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request);
    void deleteShowtime(Long id);
}
