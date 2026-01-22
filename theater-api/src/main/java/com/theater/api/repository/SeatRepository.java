package com.theater.api.repository;

import com.theater.api.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreenIdOrderByRowLabelAscSeatNumberAsc(Long screenId);

    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId AND s.id NOT IN " +
           "(SELECT rs.seat.id FROM ReservedSeat rs WHERE rs.reservation.showtime.id = :showtimeId " +
           "AND rs.reservation.status != 'cancelled')")
    List<Seat> findAvailableSeats(@Param("screenId") Long screenId, @Param("showtimeId") Long showtimeId);
}
