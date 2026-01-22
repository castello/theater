package com.theater.api.service;

import com.theater.api.dto.SeatDto;
import com.theater.api.dto.ShowtimeDto;
import com.theater.api.entity.Showtime;
import com.theater.api.repository.ReservedSeatRepository;
import com.theater.api.repository.SeatRepository;
import com.theater.api.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ReservedSeatRepository reservedSeatRepository;

    public ShowtimeDto getShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상영 정보를 찾을 수 없습니다: " + id));
        return ShowtimeDto.from(showtime);
    }

    public List<ShowtimeDto> getShowtimesByMovie(Long movieId, LocalDate date) {
        return showtimeRepository.findByMovieIdAndDate(movieId, date).stream()
                .map(ShowtimeDto::from)
                .toList();
    }

    public List<ShowtimeDto> getShowtimesByTheater(Long theaterId, LocalDate date) {
        return showtimeRepository.findByTheaterIdAndDate(theaterId, date).stream()
                .map(ShowtimeDto::from)
                .toList();
    }

    public List<ShowtimeDto> getShowtimesByDate(LocalDate date) {
        return showtimeRepository.findByDateRange(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
        ).stream().map(ShowtimeDto::from).toList();
    }

    public List<SeatDto> getSeatsWithAvailability(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("상영 정보를 찾을 수 없습니다: " + showtimeId));

        Long screenId = showtime.getScreen().getId();
        Set<Long> reservedSeatIds = new HashSet<>(
                reservedSeatRepository.findReservedSeatIdsByShowtimeId(showtimeId)
        );

        return seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(screenId).stream()
                .map(seat -> SeatDto.from(seat, !reservedSeatIds.contains(seat.getId())))
                .toList();
    }
}
