package com.theater.api.repository;

import com.theater.api.entity.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Long> {

    @Query("SELECT rs.seat.id FROM ReservedSeat rs WHERE rs.reservation.showtime.id = :showtimeId " +
           "AND rs.reservation.status != 'cancelled'")
    List<Long> findReservedSeatIdsByShowtimeId(@Param("showtimeId") Long showtimeId);

    List<ReservedSeat> findByReservationId(Long reservationId);
}
