package com.theater.api.controller;

import com.theater.api.dto.SeatDto;
import com.theater.api.dto.ShowtimeDto;
import com.theater.api.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeDto> getShowtime(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getShowtime(id));
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByMovie(
            @PathVariable Long movieId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showtimeService.getShowtimesByMovie(movieId, date));
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByTheater(
            @PathVariable Long theaterId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showtimeService.getShowtimesByTheater(theaterId, date));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(showtimeService.getShowtimesByDate(date));
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<SeatDto>> getSeats(@PathVariable Long id) {
        return ResponseEntity.ok(showtimeService.getSeatsWithAvailability(id));
    }
}
