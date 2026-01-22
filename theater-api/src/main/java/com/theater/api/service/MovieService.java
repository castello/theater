package com.theater.api.service;

import com.theater.api.dto.MovieDto;
import com.theater.api.entity.Movie;
import com.theater.api.entity.Movie.MovieStatus;
import com.theater.api.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(MovieDto::from)
                .toList();
    }

    public MovieDto getMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("영화를 찾을 수 없습니다: " + id));
        return MovieDto.from(movie);
    }

    public List<MovieDto> getShowingMovies() {
        return movieRepository.findByStatus(MovieStatus.showing).stream()
                .map(MovieDto::from)
                .toList();
    }

    public List<MovieDto> getUpcomingMovies() {
        return movieRepository.findByStatus(MovieStatus.upcoming).stream()
                .map(MovieDto::from)
                .toList();
    }

    public List<MovieDto> searchMovies(String keyword) {
        return movieRepository.findByTitleContaining(keyword).stream()
                .map(MovieDto::from)
                .toList();
    }
}
