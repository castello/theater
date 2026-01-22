package com.theater.api.controller;

import com.theater.api.dto.MovieDto;
import com.theater.api.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovie(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @GetMapping("/showing")
    public ResponseEntity<List<MovieDto>> getShowingMovies() {
        return ResponseEntity.ok(movieService.getShowingMovies());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieDto>> getUpcomingMovies() {
        return ResponseEntity.ok(movieService.getUpcomingMovies());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovies(@RequestParam String keyword) {
        return ResponseEntity.ok(movieService.searchMovies(keyword));
    }
}
