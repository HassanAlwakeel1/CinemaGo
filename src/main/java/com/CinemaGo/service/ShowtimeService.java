package com.CinemaGo.service;


import com.CinemaGo.model.dto.ShowtimeDTO;
import com.CinemaGo.model.dto.ShowtimeRequest;
import com.CinemaGo.model.dto.ShowtimeResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeService {
    ShowtimeResponse createShowtime(ShowtimeRequest request);
    ShowtimeResponse getShowtime(Long id);
    List<ShowtimeResponse> getAllShowtimes();
    ShowtimeResponse updateShowtime(Long id, ShowtimeRequest request);
    void deleteShowtime(Long id);
    List<ShowtimeResponse> getShowtimesByMovie(Long movieId);

    List<ShowtimeDTO> filterShowtimes(Long movieId,
                                      Long hallId,
                                      String language,
                                      String format,
                                      LocalDateTime startAfter,
                                      LocalDateTime endBefore);

}
