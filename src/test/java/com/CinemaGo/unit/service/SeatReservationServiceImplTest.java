package com.CinemaGo.unit.service;

import com.CinemaGo.model.dto.ReservationAdminResponse;
import com.CinemaGo.model.dto.ReservationRequest;
import com.CinemaGo.model.dto.ReservationResponse;
import com.CinemaGo.model.entity.SeatReservation;
import com.CinemaGo.model.entity.Showtime;
import com.CinemaGo.model.entity.Seat;
import com.CinemaGo.model.entity.User;
import com.CinemaGo.model.mapper.SeatReservationMapper;
import com.CinemaGo.repository.SeatReservationRepository;
import com.CinemaGo.repository.SeatRepository;
import com.CinemaGo.repository.ShowtimeRepository;
import com.CinemaGo.repository.UserRepository;
import com.CinemaGo.service.impl.SeatReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SeatReservationServiceImplTest {

    @Mock
    private SeatReservationRepository reservationRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SeatReservationMapper mapper;

    @InjectMocks
    private SeatReservationServiceImpl reservationService;

    private ReservationRequest request;
    private SeatReservation reservation;
    private User user;
    private Showtime showtime;
    private Seat seat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = ReservationRequest.builder()
                .showtimeId(1L)
                .seatId(2L)
                .userId(3L)
                .price(100.0)
                .build();

        user = User.builder().id(3L).firstName("John").lastName("Doe").build();
        showtime = Showtime.builder().id(1L).build();
        seat = Seat.builder().id(2L).build();

        reservation = SeatReservation.builder()
                .id(10L)
                .user(user)
                .showtime(showtime)
                .seat(seat)
                .status("PENDING")
                .build();
    }

    @Test
    void reserveSeat_SeatAlreadyReserved_ShouldReturnFailedResponse() {
        when(reservationRepository.existsByShowtimeIdAndSeatId(1L, 2L)).thenReturn(true);

        ReservationResponse response = reservationService.reserveSeat(request);

        assertEquals("FAILED", response.getStatus());
        assertEquals("Seat already reserved", response.getMessage());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void getReservationByIdForAdmin_ExistingId_ShouldReturnAdminDto() {
        ReservationAdminResponse adminDto = ReservationAdminResponse.builder().reservationId(10L).status("PENDING").build();
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(mapper.toAdminDto(reservation)).thenReturn(adminDto);

        ReservationAdminResponse result = reservationService.getReservationByIdForAdmin(10L);

        assertEquals(10L, result.getReservationId());
        verify(reservationRepository).findById(10L);
    }

    @Test
    void getReservationByIdForAdmin_NonExistingId_ShouldThrowException() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> reservationService.getReservationByIdForAdmin(99L));
    }

    @Test
    void getReservationsByFilters_ShouldApplyFiltersAndReturnList() {
        reservation.setStatus("CONFIRMED");
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));
        when(mapper.toAdminDto(reservation))
                .thenReturn(ReservationAdminResponse.builder().reservationId(10L).status("CONFIRMED").build());

        List<ReservationAdminResponse> results = reservationService.getReservationsByFilters(3L, 1L, null, "CONFIRMED");

        assertEquals(1, results.size());
        assertEquals("CONFIRMED", results.get(0).getStatus());
    }

    @Test
    void updateReservationStatus_ExistingId_ShouldUpdateAndReturnDto() {
        ReservationResponse dto = ReservationResponse.builder().reservationId(10L).status("CONFIRMED").build();
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(mapper.toDto(reservation)).thenReturn(dto);

        ReservationResponse result = reservationService.updateReservationStatus(10L, "CONFIRMED");

        assertEquals("CONFIRMED", result.getStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void updateReservationStatus_NonExistingId_ShouldThrowException() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> reservationService.updateReservationStatus(99L, "CONFIRMED"));
    }

    @Test
    void deleteReservation_ExistingId_ShouldDelete() {
        when(reservationRepository.findById(10L)).thenReturn(Optional.of(reservation));

        reservationService.deleteReservation(10L);

        verify(reservationRepository).delete(reservation);
    }

    @Test
    void deleteReservation_NonExistingId_ShouldThrowException() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> reservationService.deleteReservation(99L));
    }

    @Test
    void getReservationsByUser_ShouldReturnUserReservations() {
        when(reservationRepository.findByUserId(3L)).thenReturn(Arrays.asList(reservation));

        List<ReservationResponse> results = reservationService.getReservationsByUser(3L);

        assertEquals(1, results.size());
        assertEquals(10L, results.get(0).getReservationId());
        assertEquals("PENDING", results.get(0).getStatus());
    }
}
