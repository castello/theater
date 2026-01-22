package com.theater.api.repository;

import com.theater.api.entity.Movie;
import com.theater.api.entity.Movie.MovieStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByStatus(MovieStatus status);
    List<Movie> findByTitleContaining(String keyword);
}
