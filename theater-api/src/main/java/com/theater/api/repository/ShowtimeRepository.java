package com.theater.api.repository;

import com.theater.api.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie JOIN FETCH s.screen sc JOIN FETCH sc.theater " +
           "WHERE s.movie.id = :movieId AND DATE(s.startTime) = :date ORDER BY s.startTime")
    List<Showtime> findByMovieIdAndDate(@Param("movieId") Long movieId, @Param("date") LocalDate date);

    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie JOIN FETCH s.screen sc JOIN FETCH sc.theater " +
           "WHERE sc.theater.id = :theaterId AND DATE(s.startTime) = :date ORDER BY s.startTime")
    List<Showtime> findByTheaterIdAndDate(@Param("theaterId") Long theaterId, @Param("date") LocalDate date);

    @Query("SELECT s FROM Showtime s JOIN FETCH s.movie JOIN FETCH s.screen sc JOIN FETCH sc.theater " +
           "WHERE s.startTime >= :start AND s.startTime < :end ORDER BY s.startTime")
    List<Showtime> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
