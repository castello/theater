package com.theater.api.repository;

import com.theater.api.entity.Reservation;
import com.theater.api.entity.Reservation.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.showtime s JOIN FETCH s.movie " +
           "JOIN FETCH r.reservedSeats rs JOIN FETCH rs.seat WHERE r.user.id = :userId ORDER BY r.createdAt DESC")
    List<Reservation> findByUserIdWithDetails(@Param("userId") Long userId);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.showtime s JOIN FETCH s.movie " +
           "JOIN FETCH r.reservedSeats rs JOIN FETCH rs.seat WHERE r.id = :id")
    Optional<Reservation> findByIdWithDetails(@Param("id") Long id);
}
