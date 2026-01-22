package com.theater.api.service;

import com.theater.api.dto.ReservationDto;
import com.theater.api.dto.ReservationRequest;
import com.theater.api.entity.*;
import com.theater.api.entity.Reservation.ReservationStatus;
import com.theater.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ReservedSeatRepository reservedSeatRepository;

    @Transactional
    public ReservationDto createReservation(ReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("상영 정보를 찾을 수 없습니다"));

        // 이미 예약된 좌석 확인
        Set<Long> reservedSeatIds = new HashSet<>(
                reservedSeatRepository.findReservedSeatIdsByShowtimeId(showtime.getId())
        );

        for (Long seatId : request.getSeatIds()) {
            if (reservedSeatIds.contains(seatId)) {
                throw new IllegalStateException("이미 예약된 좌석이 포함되어 있습니다: " + seatId);
            }
        }

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());
        if (seats.size() != request.getSeatIds().size()) {
            throw new IllegalArgumentException("유효하지 않은 좌석이 포함되어 있습니다");
        }

        // 총 금액 계산
        BigDecimal totalPrice = showtime.getPrice()
                .multiply(BigDecimal.valueOf(seats.size()));

        // 예약 생성
        Reservation reservation = Reservation.builder()
                .user(user)
                .showtime(showtime)
                .totalPrice(totalPrice)
                .status(ReservationStatus.pending)
                .build();

        // 좌석 예약
        for (Seat seat : seats) {
            ReservedSeat reservedSeat = ReservedSeat.builder()
                    .seat(seat)
                    .build();
            reservation.addReservedSeat(reservedSeat);
        }

        reservationRepository.save(reservation);

        return ReservationDto.from(reservation);
    }

    public ReservationDto getReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다: " + id));
        return ReservationDto.from(reservation);
    }

    public List<ReservationDto> getUserReservations(Long userId) {
        return reservationRepository.findByUserIdWithDetails(userId).stream()
                .map(ReservationDto::from)
                .toList();
    }

    @Transactional
    public ReservationDto confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다: " + id));

        if (reservation.getStatus() != ReservationStatus.pending) {
            throw new IllegalStateException("대기 중인 예약만 확정할 수 있습니다");
        }

        reservation.setStatus(ReservationStatus.confirmed);
        return ReservationDto.from(reservation);
    }

    @Transactional
    public ReservationDto cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다: " + id));

        if (reservation.getStatus() == ReservationStatus.cancelled) {
            throw new IllegalStateException("이미 취소된 예약입니다");
        }

        reservation.setStatus(ReservationStatus.cancelled);

        // 결제가 있으면 환불 처리
        if (reservation.getPayment() != null) {
            reservation.getPayment().refund();
        }

        return ReservationDto.from(reservation);
    }
}
