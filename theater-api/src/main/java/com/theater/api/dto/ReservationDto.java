package com.theater.api.dto;

import com.theater.api.entity.Reservation;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private Long id;
    private Long userId;
    private Long showtimeId;
    private String movieTitle;
    private String theaterName;
    private String screenName;
    private LocalDateTime showStartTime;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private List<String> seatCodes;
    private PaymentDto payment;

    public static ReservationDto from(Reservation reservation) {
        ReservationDto.ReservationDtoBuilder builder = ReservationDto.builder()
                .id(reservation.getId())
                .userId(reservation.getUser().getId())
                .showtimeId(reservation.getShowtime().getId())
                .movieTitle(reservation.getShowtime().getMovie().getTitle())
                .theaterName(reservation.getShowtime().getScreen().getTheater().getName())
                .screenName(reservation.getShowtime().getScreen().getName())
                .showStartTime(reservation.getShowtime().getStartTime())
                .totalPrice(reservation.getTotalPrice())
                .status(reservation.getStatus().name())
                .createdAt(reservation.getCreatedAt())
                .seatCodes(reservation.getReservedSeats().stream()
                        .map(rs -> rs.getSeat().getSeatCode())
                        .toList());

        if (reservation.getPayment() != null) {
            builder.payment(PaymentDto.from(reservation.getPayment()));
        }

        return builder.build();
    }
}
