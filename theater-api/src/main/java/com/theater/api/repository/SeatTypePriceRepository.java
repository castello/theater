package com.theater.api.repository;

import com.theater.api.entity.Seat;
import com.theater.api.entity.SeatTypePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatTypePriceRepository extends JpaRepository<SeatTypePrice, Seat.SeatType> {
    Optional<SeatTypePrice> findBySeatTypeAndActiveTrue(Seat.SeatType seatType);
}
